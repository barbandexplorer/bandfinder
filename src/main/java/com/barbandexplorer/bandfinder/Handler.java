package com.barbandexplorer.bandfinder;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.barbandexplorer.bandfinder.handler.HandlerRequest;
import com.barbandexplorer.bandfinder.pipeline.Pipeline;

public class Handler implements RequestHandler<HandlerRequest,Boolean> {

    private Pipeline pipeline;

    public Handler() {
        pipeline = new Pipeline();
    }

    @Override
    public Boolean handleRequest(HandlerRequest handlerRequest, Context context) {

        try {
            pipeline.processRequest(handlerRequest.getPlayListId());
            return true;
        }catch (Exception e){

            return false;
        }

    }
}
