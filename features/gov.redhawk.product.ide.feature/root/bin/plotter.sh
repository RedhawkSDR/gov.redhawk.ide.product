#!/bin/bash
MY_DIR=`dirname $0`
WORKSPACE_LOC=$HOME/.redhawk/plotter
echo "$MY_DIR/../eclipse -nosplash -data $WORKSPACE_LOC -application gov.redhawk.plotter.application.plotter -pluginCustomization $MY_DIR/plotter_customization.ini $@
rm -rf $WORKSPACE_LOC"
$MY_DIR/../eclipse -nosplash -data $WORKSPACE_LOC -application gov.redhawk.plotter.application.plotter -pluginCustomization $MY_DIR/plotter_customization.ini $@
rm -rf $WORKSPACE_LOC
