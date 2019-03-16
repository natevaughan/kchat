/*
 * This file is generated by jOOQ.
 */
package com.natevaughan.kchat.domain.jooq.tables.records;


import com.natevaughan.kchat.domain.jooq.tables.Chat;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


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
public class ChatRecord extends UpdatableRecordImpl<ChatRecord> implements Record5<Long, Timestamp, String, String, Long> {

    private static final long serialVersionUID = -1108428363;

    /**
     * Setter for <code>kchat.chat.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>kchat.chat.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>kchat.chat.date_created</code>.
     */
    public void setDateCreated(Timestamp value) {
        set(1, value);
    }

    /**
     * Getter for <code>kchat.chat.date_created</code>.
     */
    public Timestamp getDateCreated() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>kchat.chat.access_key</code>.
     */
    public void setAccessKey(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>kchat.chat.access_key</code>.
     */
    public String getAccessKey() {
        return (String) get(2);
    }

    /**
     * Setter for <code>kchat.chat.name</code>.
     */
    public void setName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>kchat.chat.name</code>.
     */
    public String getName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>kchat.chat.creator_id</code>.
     */
    public void setCreatorId(Long value) {
        set(4, value);
    }

    /**
     * Getter for <code>kchat.chat.creator_id</code>.
     */
    public Long getCreatorId() {
        return (Long) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<Long, Timestamp, String, String, Long> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<Long, Timestamp, String, String, Long> valuesRow() {
        return (Row5) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Chat.CHAT.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field2() {
        return Chat.CHAT.DATE_CREATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Chat.CHAT.ACCESS_KEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Chat.CHAT.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return Chat.CHAT.CREATOR_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component2() {
        return getDateCreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getAccessKey();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component5() {
        return getCreatorId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value2() {
        return getDateCreated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getAccessKey();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value5() {
        return getCreatorId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChatRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChatRecord value2(Timestamp value) {
        setDateCreated(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChatRecord value3(String value) {
        setAccessKey(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChatRecord value4(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChatRecord value5(Long value) {
        setCreatorId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChatRecord values(Long value1, Timestamp value2, String value3, String value4, Long value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ChatRecord
     */
    public ChatRecord() {
        super(Chat.CHAT);
    }

    /**
     * Create a detached, initialised ChatRecord
     */
    public ChatRecord(Long id, Timestamp dateCreated, String accessKey, String name, Long creatorId) {
        super(Chat.CHAT);

        set(0, id);
        set(1, dateCreated);
        set(2, accessKey);
        set(3, name);
        set(4, creatorId);
    }
}