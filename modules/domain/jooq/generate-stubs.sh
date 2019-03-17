#! /bin/bash
java -classpath jooq-3.11.10.jar:jooq-meta-3.11.10.jar:jooq-codegen-3.11.10.jar:mysql-connector-java-8.0.15.jar:. org.jooq.codegen.GenerationTool kchat.xml