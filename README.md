# kchat
Chat server using kotlin, jersey, and jooq

Current version: 0.1.0

### Create a Database Schema
From Unix/OSX, replacing `<YOUR_USER>` with a valid MySQL user with create permissions:
```
cd modules/domain/jooq
mysql -u<YOUR_USER> -p < kchat.sql
```
Enter your MySQL password when prompted

### Configure MySQL Credentials 
Open `src/main/resources/defaults.properties` and edit the following properties to your MySQL user/password:
```
jdbc.user=root
jdbc.pass=
```

### Configure an Admin User
The app will create an admin user on startup (if one does not exist). To edit the username and API key for the default user, edit the following properties in `defaults.properties`:
```
admin.name=localadmin
admin.token=local.token
```

### Build and Run
Build and run the app:
```
gradle fatJar
java -jar build/libs/kchat-all-0.1.0.jar
```