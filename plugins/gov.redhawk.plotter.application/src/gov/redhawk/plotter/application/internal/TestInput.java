/*******************************************************************************
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package gov.redhawk.plotter.application.internal;

import java.util.Random;

import bulkio.time.utils;
import BULKIO.PrecisionUTCTime;
import BULKIO.StreamSRI;
import BULKIO.dataDouble;
import BULKIO.dataDoubleHelper;
import CF.DataType;
import CF.PortOperations;
import CF.PortPackage.InvalidPort;
import CF.PortPackage.OccupiedPort;

public enum TestInput implements PortOperations, Runnable {
	INSTANCE;

	private dataDouble output;
	private String connectionID;

	@Override
	public void connectPort(org.omg.CORBA.Object connection, String connectionId) throws InvalidPort, OccupiedPort {
		this.output = dataDoubleHelper.narrow(connection);
		this.connectionID = connectionId;
	}
	
	public String getConnectionID() {
		return connectionID;
	}

	@Override
	public void disconnectPort(String connectionId) throws InvalidPort {
		// TODO Auto-generated method stub

	}

	private StreamSRI sri;
	private String streamID = "data";
	private Random random = new Random();

	@Override
	public void run() {
		while (true) {
			if (output != null) {
				if (this.sri == null) {
					sri = new StreamSRI(1, 0.0, 1.0, (short) 1, 0, 0.0, 0.0, (short) 0, (short) 0, streamID, false, new DataType[0]);
					output.pushSRI(sri);
				}
				PrecisionUTCTime time = utils.now();
				double[] data = new double[1024];
				for (int i = 0; i < data.length; i++) {
					data[i] = random.nextDouble();
				}
				output.pushPacket(data, time, false, streamID);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// PASS
				}
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// PASS
				}
			}
		}

	}

}
