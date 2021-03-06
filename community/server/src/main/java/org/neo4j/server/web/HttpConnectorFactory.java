/**
 * Copyright (c) 2002-2014 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.server.web;

import org.eclipse.jetty.server.*;

import java.util.Arrays;

public class HttpConnectorFactory
{

    public ConnectionFactory createHttpConnectionFactory()
    {
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setRequestHeaderSize( 20 * 1024 );
        httpConfig.setResponseHeaderSize( 20 * 1024 );
        return new HttpConnectionFactory( httpConfig );
    }

    public ServerConnector createConnector( Server server, String host, int port, int jettyMaxThreads )
    {
        ConnectionFactory httpFactory = createHttpConnectionFactory();
        return createConnector(server, host, port, jettyMaxThreads, httpFactory );
    }

    public ServerConnector createConnector( Server server, String host, int port, int jettyMaxThreads, ConnectionFactory... httpFactories )
    {
        // Note: 1/4 accept, 3/4 select
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int cpusByConfiguredThreads = jettyMaxThreads / 10;
        int cpusToConsider = Math.max(1, Math.min( availableProcessors, cpusByConfiguredThreads ) );
        int acceptors = Math.max( 1 ,cpusToConsider / 4 );
        int selectors = Math.max( 1, cpusToConsider - acceptors );

        ServerConnector connector = new ServerConnector( server , null, null, null, acceptors, selectors, httpFactories );

        connector.setConnectionFactories( Arrays.asList( httpFactories ) );

        // TCP backlog, per socket, 50 is the default, consider adapting if needed
        connector.setAcceptQueueSize( 50 );

        connector.setHost( host );
        connector.setPort( port );

        return connector;
    }
}
