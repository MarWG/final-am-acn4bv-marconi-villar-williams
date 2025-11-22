COMO OBTENER EL CÓDIGO SHA1 EN CASO DE NECESITARLO PARA AGREGAR A FIREBASE.

Para Windows:

### Opción 1: Usando PowerShell (Más fácil)
1-Abrir PowerShell
2-Copiar y pegar este comando:
	C:\Program Files\Android\Android Studio\jbr\bin\keytool.exe" -list -v -keystore "$env:USERPROFILE\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
	## Acá cuidado con el path, si tu Android Studio está instalado en otra carpeta que no sea "C:\Program Files"
	
3- Buscar la línea que dice **SHA1:** y copiar el código completo
   - Se verá algo así: SHA1: C7:2F:7E:F8:00:95:F1:84:1F:A4:B4:5A:C6:B2:80:70:C9:54:6A:A9



### Opción 2: Usando Android Studio (Más confiable)
1. Abrir el proyecto en **Android Studio**
2. En el panel derecho, abrir **Gradle** (o ir a View → Tool Windows → Gradle)
3. Navegar a: **EternalGames → Tasks → android → signingReport**
4. Hacer **doble clic** en **signingReport**
5. En la ventana **Run** (abajo), buscar:

	Variant: debug
	Config: debug
	Store: C:\Users\[usuario]\.android\debug.keystore
	Alias: androiddebugkey
	SHA1: XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX

6. Copiar todo el código SHA1



Para Mac/Linux:


Abrir Terminal y ejecutar:
	keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
2. Buscar y copiar la línea **SHA1:**

Una vez obtenido el código SHA-1, enviárnoslo por correo/mensaje. Listo! 

**Ejemplo del formato:*
SHA1: C7:2F:7E:F8:00:95:F1:84:1F:A4:B4:5A:C6:B2:80:70:C9:54:6A:A9

