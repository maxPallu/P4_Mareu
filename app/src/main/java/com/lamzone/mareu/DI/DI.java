package com.lamzone.mareu.DI;

import com.lamzone.mareu.service.MeetingAPI;
import com.lamzone.mareu.service.MeetingAPIService;

public class DI {

    private static MeetingAPI service = new MeetingAPIService();

    public static MeetingAPI getMeetingApiService() {
        return service;
    }

}
