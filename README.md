# kchat
Open-source chat server built with kotlin, jersey, and jooq

Current version: 0.1.0

### Requirements
 - Mysql 5.7 or higher
 - Java 1.8 or higher

### Create a Database Schema
From Unix/OSX, replacing `<YOUR_USER>` with a valid MySQL user with create permissions:
```
mysql -u<YOUR_USER> -p < modules/domain/jooq/kchat.sql
```
Enter your MySQL password when prompted

### Configure MySQL Credentials 
Open `src/main/resources/defaults.properties` and edit the following properties, replacing `<YOUR_USER>` and `<YOUR_PASSWORD>` to your MySQL user/password:
```
jdbc.user=<YOUR_USER>
jdbc.pass=<YOUR_PASSWORD>
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
./gradleW fatJar
java -jar build/libs/kchat-all-0.1.0.jar
```