package com.arpaul.geocare.dataObject;

import java.io.Serializable;

/**
 * Created by Aritra on 21-10-2016.
 */

public class MessageDO implements Serializable {
    public String messageSender = "";
    public String messageBody = "";

    public static final String SENDER = "SENDER";
    public static final String BODY = "BODY";
}
