package com.epam.wilma.extras.grpc;
/*==========================================================================
Copyright since 2022, EPAM Systems

This file is part of Wilma.

Wilma is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wilma is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wilma.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import com.epam.wilma.extras.grpc.server.GrpcProperties;
import com.epam.wilma.extras.grpc.server.GrpcServer;
import io.grpc.Server;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * The main class of this service.
 *
 * @author tkohegyi
 */
public class GRPCServiceCore {

    private final Logger logger = LoggerFactory.getLogger(GRPCServiceCore.class);

    protected String handlePostMockRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String response = "{ \"status\": \"OK\" }";
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        try {
            String body = httpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            JSONObject object = new JSONObject(body);
            if (object.has("startService")) { //start the service
                startService();
            }
            if (object.has("stopService")) { //stop the service
            }
        } catch (JSONException | IOException e) {
            response = "{ \"status\": \"BAD REQUEST\" }";
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.warn("GRPCService had an incorrect call.", e);
        }
        return response;
    }

    protected String getGRPCServiceStatus() {
        return "";
    }

    private String startService() throws IOException {
        GrpcServer grpcServer = GrpcServer.getInstance();
        if (grpcServer != null) {
            GrpcProperties.ServerProperties serverProperties = new GrpcProperties.ServerProperties(50000, 50000, 50000, 50000, 50000);
            GrpcProperties.ErrorCodeMapping.Http http = new GrpcProperties.ErrorCodeMapping.Http();
            GrpcProperties.ErrorCodeMapping errorCodeMapping = new GrpcProperties.ErrorCodeMapping(http);
            GrpcProperties grpcProperties = new GrpcProperties(serverProperties, errorCodeMapping);
            grpcServer.configureGrpcServer(grpcProperties, null );
            grpcServer.start();
        }
        return "{ \"status\": \"OK\" }";
    }
}
