# sync-physx-spigot
Версия - 1.19.4

Это spigot-плагин, написанный на джаве для симуляции физики, используя PhysX и Item-Displays.

Текущие функции:
 - Симуляция блоков
 - Взрывы
 - Парсинг мира
 - Симуляция тела игрока
 - Ломание/Вставка блоков

Демонстрация: https://www.youtube.com/watch?v=Bv14fF_akkY

Из-за платформозависимости PhysX плагин может не работать на архитектуре arm64/arm и т.п.
Плагин поддерживает только сервера, стоящие на машинах x86. Для запуска на своей машине скомпилируйте physx-jni под себя https://github.com/fabmax/physx-jni и соберите плагин под себя
