# 📦 Instrucciones de Distribución - Resolvedor de Laberintos

## 🎯 Para Distribuir a Otras Personas

### **Archivos Necesarios:**
- `MazeSolver.jar` (archivo principal)
- `Ejecutar_MazeSolver.bat` (opcional, para Windows)

### **Requisitos del Sistema:**
- **Java 8 o superior** instalado
- **Memoria RAM:** Mínimo 512MB
- **Sistema Operativo:** Windows, macOS, Linux

## 🚀 Formas de Ejecutar

### **Opción 1: Doble Clic (Windows)**
1. Copia `MazeSolver.jar` a la computadora destino
2. Haz doble clic en `Ejecutar_MazeSolver.bat`
3. ¡Listo! La aplicación se ejecutará automáticamente

### **Opción 2: Línea de Comandos**
```bash
java -jar MazeSolver.jar
```

### **Opción 3: PowerShell (Windows)**
```powershell
java -jar MazeSolver.jar
```

## 🔧 Verificar Instalación de Java

### **Windows:**
```cmd
java -version
```

### **macOS/Linux:**
```bash
java -version
```

**Resultado esperado:**
```
java version "1.8.0_XXX" 2018-XX-XX
Java(TM) SE Runtime Environment (build 1.8.0_XXX-bXXX)
Java HotSpot(TM) 64-Bit Server VM (build 25.XXX-bXXX, mixed mode)
```

## 📁 Estructura de Archivos para Distribución

```
Carpeta_Distribucion/
├── MazeSolver.jar              # JAR ejecutable
├── Ejecutar_MazeSolver.bat     # Lanzador (Windows)
└── INSTRUCCIONES_DISTRIBUCION.md  # Este archivo
```

## 🎮 Características del JAR

### **✅ Incluye Todo:**
- Todas las clases Java compiladas
- Imágenes de fondo (inicio.jpg, integrantes.jpg, pasto.jpg)
- Iconos (flecha.png)
- Bibliotecas JFreeChart
- Tema visual Minecraft completo

### **✅ Funcionalidades:**
- 5 algoritmos de resolución de laberintos
- Edición manual de paredes
- Estadísticas y gráficos
- Persistencia de datos en CSV
- Interfaz completamente en español

## 🛠️ Solución de Problemas

### **Error: "java no se reconoce como comando"**
- **Solución:** Instalar Java desde [java.com](https://www.java.com)

### **Error: "No se puede cargar la clase principal"**
- **Solución:** Verificar que el JAR no esté corrupto
- **Solución:** Reinstalar Java

### **Error: "No se pueden cargar las imágenes"**
- **Solución:** El JAR incluye las imágenes internamente
- **Solución:** Verificar que el JAR esté completo

### **Error: "No se abre la ventana de gráficas"**
- **Solución:** El JAR incluye JFreeChart completo internamente
- **Solución:** Verificar que el JAR esté completo (debe ser ~2.5MB)
- **Solución:** Todas las clases de JFreeChart están extraídas e incluidas

### **Error: "Permisos denegados" (Linux/macOS)**
```bash
chmod +x MazeSolver.jar
```

## 📊 Información Técnica

### **Tamaño del JAR:** ~2.5MB (incluye JFreeChart)
### **Dependencias Incluidas:**
- JFreeChart (gráficos)
- Todas las imágenes y recursos

### **Algoritmos Implementados:**
1. **BFS** - Breadth-First Search
2. **DFS** - Depth-First Search  
3. **Recursivo (2 direcciones)**
4. **Recursivo (4 direcciones)**
5. **Recursivo (4 direcciones + Backtracking)**

## 🎯 Flujo de la Aplicación

1. **SplashScreen** - Pantalla de carga (5 segundos)
2. **IntermediatePanel** - Panel con imagen de integrantes
3. **MazeView** - Aplicación principal con todas las funcionalidades

## 📞 Soporte

Si tienes problemas:
1. Verifica que Java esté instalado correctamente
2. Ejecuta desde línea de comandos para ver errores
3. Asegúrate de que el JAR esté completo

---

**¡Disfruta resolviendo laberintos con estilo Minecraft!** 🎮 