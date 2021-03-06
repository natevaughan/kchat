/*
 * This file is generated by jOOQ.
 */
package com.natevaughan.kchat.domain.jooq.tables;


import com.natevaughan.kchat.domain.jooq.Indexes;
import com.natevaughan.kchat.domain.jooq.Kchat;
import com.natevaughan.kchat.domain.jooq.Keys;
import com.natevaughan.kchat.domain.jooq.tables.records.ChatRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Chat extends TableImpl<ChatRecord> {

    private static final long serialVersionUID = 367786884;

    /**
     * The reference instance of <code>kchat.chat</code>
     */
    public static final Chat CHAT = new Chat();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ChatRecord> getRecordType() {
        return ChatRecord.class;
    }

    /**
     * The column <code>kchat.chat.id</code>.
     */
    public final TableField<ChatRecord, byte[]> ID = createField("id", org.jooq.impl.SQLDataType.BINARY(16).nullable(false), this, "");

    /**
     * The column <code>kchat.chat.id_text</code>.
     */
    public final TableField<ChatRecord, String> ID_TEXT = createField("id_text", org.jooq.impl.SQLDataType.VARCHAR(36), this, "");

    /**
     * The column <code>kchat.chat.date_created</code>.
     */
    public final TableField<ChatRecord, Timestamp> DATE_CREATED = createField("date_created", org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.field("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>kchat.chat.name</code>.
     */
    public final TableField<ChatRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>kchat.chat.type</code>.
     */
    public final TableField<ChatRecord, Integer> TYPE = createField("type", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>kchat.chat.space_id</code>.
     */
    public final TableField<ChatRecord, byte[]> SPACE_ID = createField("space_id", org.jooq.impl.SQLDataType.BINARY(16).nullable(false), this, "");

    /**
     * The column <code>kchat.chat.space_id_text</code>.
     */
    public final TableField<ChatRecord, String> SPACE_ID_TEXT = createField("space_id_text", org.jooq.impl.SQLDataType.VARCHAR(36), this, "");

    /**
     * Create a <code>kchat.chat</code> table reference
     */
    public Chat() {
        this(DSL.name("chat"), null);
    }

    /**
     * Create an aliased <code>kchat.chat</code> table reference
     */
    public Chat(String alias) {
        this(DSL.name(alias), CHAT);
    }

    /**
     * Create an aliased <code>kchat.chat</code> table reference
     */
    public Chat(Name alias) {
        this(alias, CHAT);
    }

    private Chat(Name alias, Table<ChatRecord> aliased) {
        this(alias, aliased, null);
    }

    private Chat(Name alias, Table<ChatRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Chat(Table<O> child, ForeignKey<O, ChatRecord> key) {
        super(child, key, CHAT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Kchat.KCHAT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.CHAT_FK_M9ZE3MECFXQ1HXK, Indexes.CHAT_PRIMARY, Indexes.CHAT_UK_5NUHPU0CZV0E8SL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<ChatRecord> getPrimaryKey() {
        return Keys.KEY_CHAT_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ChatRecord>> getKeys() {
        return Arrays.<UniqueKey<ChatRecord>>asList(Keys.KEY_CHAT_PRIMARY, Keys.KEY_CHAT_UK_5NUHPU0CZV0E8SL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<ChatRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ChatRecord, ?>>asList(Keys.FK_M9ZE3MECFXQ1HXK);
    }

    public Space space() {
        return new Space(this, Keys.FK_M9ZE3MECFXQ1HXK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Chat as(String alias) {
        return new Chat(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Chat as(Name alias) {
        return new Chat(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Chat rename(String name) {
        return new Chat(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Chat rename(Name name) {
        return new Chat(name, null);
    }
}
