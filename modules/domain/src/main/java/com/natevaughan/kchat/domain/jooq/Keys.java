/*
 * This file is generated by jOOQ.
 */
package com.natevaughan.kchat.domain.jooq;


import com.natevaughan.kchat.domain.jooq.tables.Chat;
import com.natevaughan.kchat.domain.jooq.tables.KchatUser;
import com.natevaughan.kchat.domain.jooq.tables.Message;
import com.natevaughan.kchat.domain.jooq.tables.User;
import com.natevaughan.kchat.domain.jooq.tables.records.ChatRecord;
import com.natevaughan.kchat.domain.jooq.tables.records.KchatUserRecord;
import com.natevaughan.kchat.domain.jooq.tables.records.MessageRecord;
import com.natevaughan.kchat.domain.jooq.tables.records.UserRecord;

import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>kchat</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ChatRecord> KEY_CHAT_PRIMARY = UniqueKeys0.KEY_CHAT_PRIMARY;
    public static final UniqueKey<MessageRecord> KEY_MESSAGE_PRIMARY = UniqueKeys0.KEY_MESSAGE_PRIMARY;
    public static final UniqueKey<UserRecord> KEY_USER_PRIMARY = UniqueKeys0.KEY_USER_PRIMARY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ChatRecord, UserRecord> FKKM9ZE3MECFXQ1HXK = ForeignKeys0.FKKM9ZE3MECFXQ1HXK;
    public static final ForeignKey<KchatUserRecord, ChatRecord> FKWNOLJBCORVGUUN0P = ForeignKeys0.FKWNOLJBCORVGUUN0P;
    public static final ForeignKey<KchatUserRecord, UserRecord> FKTIA7RMNDE3JP9CHS = ForeignKeys0.FKTIA7RMNDE3JP9CHS;
    public static final ForeignKey<MessageRecord, UserRecord> FKJBUGTZ1C9YJFJDOZ = ForeignKeys0.FKJBUGTZ1C9YJFJDOZ;
    public static final ForeignKey<MessageRecord, ChatRecord> FKP7PNYAW7PXPLN82B = ForeignKeys0.FKP7PNYAW7PXPLN82B;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class UniqueKeys0 {
        public static final UniqueKey<ChatRecord> KEY_CHAT_PRIMARY = Internal.createUniqueKey(Chat.CHAT, "KEY_chat_PRIMARY", Chat.CHAT.ID);
        public static final UniqueKey<MessageRecord> KEY_MESSAGE_PRIMARY = Internal.createUniqueKey(Message.MESSAGE, "KEY_message_PRIMARY", Message.MESSAGE.ID);
        public static final UniqueKey<UserRecord> KEY_USER_PRIMARY = Internal.createUniqueKey(User.USER, "KEY_user_PRIMARY", User.USER.ID);
    }

    private static class ForeignKeys0 {
        public static final ForeignKey<ChatRecord, UserRecord> FKKM9ZE3MECFXQ1HXK = Internal.createForeignKey(com.natevaughan.kchat.domain.jooq.Keys.KEY_USER_PRIMARY, Chat.CHAT, "FKkM9zE3mecFxq1hxK", Chat.CHAT.CREATOR_ID);
        public static final ForeignKey<KchatUserRecord, ChatRecord> FKWNOLJBCORVGUUN0P = Internal.createForeignKey(com.natevaughan.kchat.domain.jooq.Keys.KEY_CHAT_PRIMARY, KchatUser.KCHAT_USER, "FKWnoljBcORVgUuN0p", KchatUser.KCHAT_USER.CHAT_ID);
        public static final ForeignKey<KchatUserRecord, UserRecord> FKTIA7RMNDE3JP9CHS = Internal.createForeignKey(com.natevaughan.kchat.domain.jooq.Keys.KEY_USER_PRIMARY, KchatUser.KCHAT_USER, "FKtia7rMnDE3JP9cHs", KchatUser.KCHAT_USER.PARTICIPANTS_ID);
        public static final ForeignKey<MessageRecord, UserRecord> FKJBUGTZ1C9YJFJDOZ = Internal.createForeignKey(com.natevaughan.kchat.domain.jooq.Keys.KEY_USER_PRIMARY, Message.MESSAGE, "FKJbUgtZ1c9YjFJDOZ", Message.MESSAGE.AUTHOR_ID);
        public static final ForeignKey<MessageRecord, ChatRecord> FKP7PNYAW7PXPLN82B = Internal.createForeignKey(com.natevaughan.kchat.domain.jooq.Keys.KEY_CHAT_PRIMARY, Message.MESSAGE, "FKp7PNYAW7PxplN82b", Message.MESSAGE.CHAT_ID);
    }
}