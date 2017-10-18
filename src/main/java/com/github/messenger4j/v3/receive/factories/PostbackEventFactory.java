package com.github.messenger4j.v3.receive.factories;

import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_AD_ID;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_ID;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_PAYLOAD;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_POSTBACK;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_RECIPIENT;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_REF;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_REFERRAL;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_SENDER;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_SOURCE;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_TIMESTAMP;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_TITLE;
import static com.github.messenger4j.internal.JsonHelper.Constants.PROP_TYPE;
import static com.github.messenger4j.internal.JsonHelper.getPropertyAsInstant;
import static com.github.messenger4j.internal.JsonHelper.getPropertyAsString;
import static com.github.messenger4j.internal.JsonHelper.hasProperty;

import com.github.messenger4j.v3.receive.PostbackEvent;
import com.github.messenger4j.v3.receive.Referral;
import com.google.gson.JsonObject;
import java.time.Instant;
import lombok.NonNull;

/**
 * @author Max Grabenhorst
 * @since 1.0.0
 */
public final class PostbackEventFactory implements BaseEventFactory<PostbackEvent> {

    @Override
    public boolean isResponsible(@NonNull JsonObject messagingEvent) {
        return hasProperty(messagingEvent, PROP_POSTBACK);
    }

    @Override
    public PostbackEvent createEventFromJson(@NonNull JsonObject messagingEvent) {
        final String senderId = getPropertyAsString(messagingEvent, PROP_SENDER, PROP_ID);
        final String recipientId = getPropertyAsString(messagingEvent, PROP_RECIPIENT, PROP_ID);
        final Instant timestamp = getPropertyAsInstant(messagingEvent, PROP_TIMESTAMP).get();
        final String title = getPropertyAsString(messagingEvent, PROP_POSTBACK, PROP_TITLE);
        final String payload = getPropertyAsString(messagingEvent, PROP_POSTBACK, PROP_PAYLOAD);

        Referral referral = null;
        if (hasProperty(messagingEvent, PROP_POSTBACK, PROP_REFERRAL)) {
            final String source = getPropertyAsString(messagingEvent, PROP_POSTBACK, PROP_REFERRAL, PROP_SOURCE);
            final String type = getPropertyAsString(messagingEvent, PROP_POSTBACK, PROP_REFERRAL, PROP_TYPE);
            final String refPayload = getPropertyAsString(messagingEvent, PROP_POSTBACK, PROP_REFERRAL, PROP_REF);
            final String adId = getPropertyAsString(messagingEvent, PROP_POSTBACK, PROP_REFERRAL, PROP_AD_ID);

            referral = new Referral(source, type, refPayload, adId);
        }

        return new PostbackEvent(senderId, recipientId, timestamp, title, payload, referral);
    }
}