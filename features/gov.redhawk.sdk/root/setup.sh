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
# REDHAWK IDE Setup script.
# To run this script in the same directory as the eclipse executable
# Ensure your current directory is in the same directory as the eclipse executable

function error() {
	echo "Setup REDHAWK IDE Failed!"
	exit 1
}

IDE_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo "Updating Eclipse.ini file:"
if grep -q -e "-Djava.endorsed.dirs="  eclipse.ini ; then
	echo "Changing line -Djava.endorsed.dirs=${IDE_HOME}/jacorb/lib" 
	sed -i -e "s#^-Djava\.endorsed\.dirs.*#-Djava.endorsed.dirs=${IDE_HOME}/jacorb/lib#" eclipse.ini || error
else 
	echo "Appending line endorsed dirs"
	echo "-Djava.endorsed.dirs=${IDE_HOME}/jacorb/lib" | tee -a eclipse.ini || error
fi

if grep -q -e "-Djacorb.config.dir="  eclipse.ini ; then
	echo "Changing line -Djacorb.config.dir=${IDE_HOME}/configuration" 
	sed -i -e "s#^-Djacorb\.config\.dir.*#-Djacorb.config.dir=${IDE_HOME}/configuration#" eclipse.ini || error
else 
	echo "Appending line config dir"
	echo "-Djacorb.config.dir=${IDE_HOME}/configuration" | tee -a eclipse.ini || error
fi

if grep -q -e "-DORBInitialPort="  eclipse.ini ; then
	echo "Changing line -DORBInitialPort=2809" 
	sed -i -e "s#^-DORBInitialPort.*#-DORBInitialPort=2809#" eclipse.ini || error
else 
	echo "Appending line config dir"
	echo "-DORBInitialPort=2809" | tee -a eclipse.ini || error
fi

if grep -q -e "-Djava.util.logging.config.file="  eclipse.ini ; then
	echo "Changing line -Djava.util.logging.config.file=${IDE_HOME}/configuration/javalogger.properties" 
	sed -i -e "s#^-Djava\.util\.logging\.config\.file.*#-Djava.util.logging.config.file=${IDE_HOME}/configuration/javalogger.properties#" eclipse.ini || error
else 
	echo "Appending line config dir"
	echo "-Djava.util.logging.config.file=${IDE_HOME}/configuration/javalogger.properties" | tee -a eclipse.ini || error
fi

if grep -q -e "-Dorg.eclipse.swt.browser.XULRunnerPath="  eclipse.ini ; then
	echo "Changing line -Dorg.eclipse.swt.browser.XULRunnerPath=${IDE_HOME}/xulrunner" 
	sed -i -e "s#^-Dorg\.eclipse\.swt\.browser\.XULRunnerPath.*#-Dorg.eclipse.swt.browser.XULRunnerPath=${IDE_HOME}/xulrunner#" eclipse.ini || error
else 
	echo "Appending line config dir"
	echo "-Dorg.eclipse.swt.browser.XULRunnerPath=${IDE_HOME}/xulrunner" | tee -a eclipse.ini || error
fi

if [ ! -d jacorb ] ; then 
	echo "Creating Jacorb Lib directory..."
	JACORB_JAR=`find plugins/ -maxdepth 1 -name org.jacorb.system*` || error 
	mkdir jacorb || error
	if [ -d "$JACORB_JAR" ] ; then 
		echo "Copying Jacorb to Lib directory..."
		mkdir jacorb/lib || error
		cp -R $JACORB_JAR/jars/* jacorb/lib/. || error
	else 
		echo "Extracting Jacorb to Lib directory..."
		cd jacorb 
		jar xf ../$JACORB_JAR || error
	fi
fi

