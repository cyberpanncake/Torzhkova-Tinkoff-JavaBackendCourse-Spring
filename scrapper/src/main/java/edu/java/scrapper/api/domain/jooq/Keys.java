/*
 * This file is generated by jOOQ.
 */

package edu.java.scrapper.api.domain.jooq;

import edu.java.scrapper.api.domain.jooq.tables.Chat;
import edu.java.scrapper.api.domain.jooq.tables.Link;
import edu.java.scrapper.api.domain.jooq.tables.Subscription;
import edu.java.scrapper.api.domain.jooq.tables.records.ChatRecord;
import edu.java.scrapper.api.domain.jooq.tables.records.LinkRecord;
import edu.java.scrapper.api.domain.jooq.tables.records.SubscriptionRecord;
import javax.annotation.processing.Generated;
import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

/**
 * A class modelling foreign key relationships and constraints of tables in the
 * default schema.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ChatRecord> CONSTRAINT_1 =
        Internal.createUniqueKey(Chat.CHAT, DSL.name("CONSTRAINT_1"), new TableField[] {Chat.CHAT.ID}, true);
    public static final UniqueKey<ChatRecord> CONSTRAINT_1F =
        Internal.createUniqueKey(Chat.CHAT, DSL.name("CONSTRAINT_1F"), new TableField[] {Chat.CHAT.TG_ID}, true);
    public static final UniqueKey<LinkRecord> CONSTRAINT_2 =
        Internal.createUniqueKey(Link.LINK, DSL.name("CONSTRAINT_2"), new TableField[] {Link.LINK.ID}, true);
    public static final UniqueKey<LinkRecord> CONSTRAINT_23 =
        Internal.createUniqueKey(Link.LINK, DSL.name("CONSTRAINT_23"), new TableField[] {Link.LINK.URL}, true);
    public static final UniqueKey<SubscriptionRecord> CONSTRAINT_9 = Internal.createUniqueKey(Subscription.SUBSCRIPTION,
        DSL.name("CONSTRAINT_9"),
        new TableField[] {Subscription.SUBSCRIPTION.CHAT_ID, Subscription.SUBSCRIPTION.LINK_ID},
        true
    );

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<SubscriptionRecord, ChatRecord> CONSTRAINT_9E = Internal.createForeignKey(
        Subscription.SUBSCRIPTION,
        DSL.name("CONSTRAINT_9E"),
        new TableField[] {Subscription.SUBSCRIPTION.CHAT_ID},
        Keys.CONSTRAINT_1,
        new TableField[] {Chat.CHAT.ID},
        true
    );
    public static final ForeignKey<SubscriptionRecord, LinkRecord> CONSTRAINT_9E7 = Internal.createForeignKey(
        Subscription.SUBSCRIPTION,
        DSL.name("CONSTRAINT_9E7"),
        new TableField[] {Subscription.SUBSCRIPTION.LINK_ID},
        Keys.CONSTRAINT_2,
        new TableField[] {Link.LINK.ID},
        true
    );
}