Сборка (Minecraft 1.21.11, Fabric, нужна Java 21):
1. Скопируй gradlew, gradlew.bat и папку gradle/ из шаблона fabricmc.net/develop/template
   в эту папку (или используй установленный Gradle 9.x).
2. Выполни:  ./gradlew build
3. Готовый мод: build/libs/fastuse-1.0.0.jar
4. Положи его в папку mods вместе с Fabric API 0.139.4+1.21.11 (или новее для 1.21.11).
Если Gradle ругается на версию зависимости - возьми актуальную на fabricmc.net/develop
и поправь gradle.properties.
