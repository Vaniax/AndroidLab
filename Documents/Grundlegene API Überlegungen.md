# Basic 

GetFriends(userId) -> liefert alle friends 
GetAppointments(userId) -> liefert alle Öffentlichen Appointments + die privaten zu denen der user eingeladen ist
GetFriendLocation(userId, friendId) -> eine Location eines freundes
GetFriendsLocations(userId) -> Location aller freunde
GetMessages(userId) -> alle Messages von und an userId (für Messageverläufe)
SendMessage(userId, targetId, Message) -> Message von user an target
UpdateLocation(userID) -> update der eigenen Location



# Login

login(passwort, name) -> return session token


# Token:

IP
Auslaufzeit (vll. bei nichtinteraktion mit Server)
Unique stringkette für Useridentifizierung
evtl. als "AccessToken" ausgelegt -> Rechte. Also wenn man sich als Sysadmin einlogt kann man tolle extrasachen machen