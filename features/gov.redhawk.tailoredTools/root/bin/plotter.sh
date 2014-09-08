#!/bin/bash
###############################################################################
# This file is protected by Copyright. 
# Please refer to the COPYRIGHT file distributed with this source distribution.
#
# This file is part of REDHAWK IDE.
#
# All rights reserved.  This program and the accompanying materials are made available under 
# the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at 
# http://www.eclipse.org/legal/epl-v10.html
###############################################################################
MY_DIR=`dirname $0`
WORKSPACE_LOC=$HOME/.redhawk/plotter
COMMAND="$MY_DIR/../eclipse -nosplash -data $WORKSPACE_LOC -name Plotter -clearPersistedState -product gov.redhawk.plotter.application.product -application gov.redhawk.plotter.application.plotter $@"
#echo $COMMAND
$COMMAND
