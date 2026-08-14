-- exportar-cajas.lua
-- Exporta las cajas de colisión (colbox, hitbox, hurtbox) de un sprite
-- a un archivo JSON con los bounds por frame, agrupados por tag y capa.
--
-- Uso en CLI:
--   aseprite -b knight.aseprite -script exportar-cajas.lua --script-param salida=knight-cajas.json
--
-- Convención de capas: "colbox", "hitbox", "hurtbox"
-- (alguna capa puede no existir o no tener cels en todos los frames)

local spr = app.activeSprite
if not spr then
  print("Error: no hay un sprite activo")
  return
end

-- Parámetro de salida via --script-param
local archivoSalida = app.params["salida"]
if not archivoSalida or archivoSalida == "" then
  archivoSalida = "cajas.json"
end

local TIPOS = { "colbox", "hitbox", "hurtbox" }

-- Recolecta los tags
local tags = {}
for _, tag in ipairs(spr.tags) do
  table.insert(tags, {
    nombre = tag.name,
    desde = tag.fromFrame.frameNumber - 1, -- 0-based
    hasta = tag.toFrame.frameNumber - 1    -- 0-based
  })
end

local resultado = {}

for _, tipo in ipairs(TIPOS) do
  -- Buscar la capa por nombre
  local capa = nil
  for _, l in ipairs(spr.layers) do
    if l.name == tipo and l.isImage then
      capa = l
      break
    end
  end

  if capa then
    local porTag = {}
    for _, tag in ipairs(tags) do
      local frames = {}
      for f = tag.desde, tag.hasta do
        local cel = capa:cel(f + 1) -- cel() usa frames 1-based
        if cel then
          local b = cel.bounds
          -- frame relativo al tag (0-based)
          table.insert(frames, {
            frame = f - tag.desde,
            x = b.x,
            y = b.y,
            w = b.width,
            h = b.height
          })
        end
      end
      if #frames > 0 then
        porTag[tag.nombre] = frames
      end
    end
    if next(porTag) then
      resultado[tipo] = porTag
    end
  end
end

-- Serializar a JSON manualmente (evita dependencias externas)
local function jsonEscape(s)
  s = tostring(s)
  s = s:gsub('\\', '\\\\')
  s = s:gsub('"', '\\"')
  return s
end

local function serializarFrame(f)
  return string.format('{ "frame": %d, "x": %d, "y": %d, "w": %d, "h": %d }',
    f.frame, f.x, f.y, f.w, f.h)
end

local partes = {}
local primerTipo = true
for _, tipo in ipairs(TIPOS) do
  local porTag = resultado[tipo]
  if porTag then
    if not primerTipo then
      table.insert(partes, ',')
    end
    primerTipo = false
    table.insert(partes, '  "' .. tipo .. '": {\n')

    local primerTag = true
    for nombre, frames in pairs(porTag) do
      if not primerTag then
        table.insert(partes, ',\n')
      end
      primerTag = false
      table.insert(partes, '    "' .. jsonEscape(nombre) .. '": [\n')
      for i, f in ipairs(frames) do
        table.insert(partes, '      ' .. serializarFrame(f))
        if i < #frames then
          table.insert(partes, ',')
        end
        table.insert(partes, '\n')
      end
      table.insert(partes, '    ]')
    end
    table.insert(partes, '\n  }')
  end
end

local json = '{\n' .. table.concat(partes) .. '\n}\n'

local archivo = io.open(archivoSalida, "w")
if not archivo then
  print("Error: no se pudo abrir " .. archivoSalida)
  return
end
archivo:write(json)
archivo:close()

print("Cajas exportadas a " .. archivoSalida)
