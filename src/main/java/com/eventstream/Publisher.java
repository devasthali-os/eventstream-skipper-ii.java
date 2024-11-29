package com.eventstream;

import com.eventstream.api.Event;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface Publisher {

    Event publish(Event message)  throws IOException, TimeoutException;;

}
