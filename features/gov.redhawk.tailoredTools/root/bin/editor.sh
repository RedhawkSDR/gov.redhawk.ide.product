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
WORKSPACE_LOC=~/.redhawk/editor
#echo $COMMAND
COMMAND="$MY_DIR/../eclipse -nosplash -data $WORKSPACE_LOC -name Rheditor -product gov.redhawk.editor.product.ide -application gov.redhawk.editor.product.app -clearPersistedState --launcher.defaultAction openFile $@"
$COMMAND
