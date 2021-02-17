# Нетология. Разработка приложений на Kotlin. 

## Урок 9 (Лямбды, extension-функции, операторы). Задача 1. ChatService

### [Задание](https://github.com/netology-code/kt-homeworks/tree/master/09_lambda):

Разработать приложение для личных сообщений. Требования:

1. Должны быть чаты (чат - это общение с одним человеком, т.н. direct messages)
2. Можно создавать чаты, удалять их, получать список имеющихся чатов
3. В каждом чате есть сообщения от 1 до нескольких (см. раздел ниже)
4. Имеется возможность создавать сообщения, редактировать их и удалять (для простоты - можно удалять и свои, и чужие)
5. В каждой чате есть прочитанные и не прочитанные сообщения


Пользователь должен иметь возможность:

1. Получить информацию о количестве непрочитанных чатов (например, service.getUnreadChatsCount) - это количество чатов, в каждом из которых есть хотя бы одно непрочитанное сообщение
2. Получить список чатов (например, service.getChats) - где в каждом чате есть последнее сообщение (если нет, то пишется нет сообщений)
3. Получить список сообщений из чата, указав (после того, как вызвана данная функция, все отданные сообщения автоматически считаются прочитанными):
	- id чата
	- id последнего сообщения, начиная с которого нужно подгрузить более новые
	- количество сообщений
4. Создать новое сообщение
5. Удалить сообщение (при удалении последнего сообщения в чате весь чат удаляется)
6. Создать чат (чат создаётся тогда, когда пользователю, с которым до этого не было чата, отправляется первое сообщение)
7. Удалить чат (целиком удаляется все переписка)

## Урок 10 (Sequences). Задача 1. ChatService Optimization

### [Продолэжение задания](https://github.com/netology-code/kt-homeworks/tree/master/10_sequences):

Перевести проект на Sequence