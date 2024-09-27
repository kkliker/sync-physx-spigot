# sync-physx-spigot
Версия - 1.19.4
(Может заработает на версиях и выше, но я не тестил)

Это плагин, написанный на spigot для симуляции физики, используя physx и displays.
Вы можете делать форк/изменять код как вам угодно.

Текущие функции:
 - Симуляция PxRigidDynamic (блоков)
 - Взрывы
 - Парсинг мира в PxRigidStatic
 - Симуляция тела игрока
 - Ломание/Вставка блоков

Демонстрация: https://www.youtube.com/watch?v=Bv14fF_akkY

Из-за платформозависимости PhysX плагин может не работать на архитектуре arm64/arm и т.п.
Плагин поддерживает только сервера, стоящие на машинах x86. Для запуска на своей машине скомпилируйте physx-jni под себя https://github.com/fabmax/physx-jni и укажите зависимость в build.gradle, либо скачайте готовый релиз плагина для x86.
