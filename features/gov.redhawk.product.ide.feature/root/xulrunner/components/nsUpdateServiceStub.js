/* -*- Mode: C++; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 2 -*- */
/*
//@line 38 "/builds/slave/rel-m-192-xr-lnx-bld/build/toolkit/mozapps/update/src/nsUpdateServiceStub.js"
*/
Components.utils.import("resource://gre/modules/XPCOMUtils.jsm");
Components.utils.import("resource://gre/modules/FileUtils.jsm");

const Ci = Components.interfaces;

const DIR_UPDATES         = "updates";
const FILE_UPDATE_STATUS  = "update.status";

const KEY_APPDIR          = "XCurProcD";
//@line 53 "/builds/slave/rel-m-192-xr-lnx-bld/build/toolkit/mozapps/update/src/nsUpdateServiceStub.js"

/**
//@line 61 "/builds/slave/rel-m-192-xr-lnx-bld/build/toolkit/mozapps/update/src/nsUpdateServiceStub.js"
 */
function getUpdateDirNoCreate(pathArray) {
//@line 72 "/builds/slave/rel-m-192-xr-lnx-bld/build/toolkit/mozapps/update/src/nsUpdateServiceStub.js"
  return FileUtils.getDir(KEY_APPDIR, pathArray, false);
}

function UpdateServiceStub() {
  let statusFile = getUpdateDirNoCreate([DIR_UPDATES, "0"]);
  statusFile.append(FILE_UPDATE_STATUS);
  // If the update.status file exists then initiate post update processing.
  if (statusFile.exists()) {
    let aus = Components.classes["@mozilla.org/updates/update-service;1"].
              getService(Ci.nsIApplicationUpdateService).
              QueryInterface(Ci.nsIObserver);
    aus.observe(null, "post-update-processing", "");
  }
}
UpdateServiceStub.prototype = {
  classDescription: "Update Service Stub",
  contractID: "@mozilla.org/updates/update-service-stub;1",
  classID: Components.ID("{e43b0010-04ba-4da6-b523-1f92580bc150}"),
  _xpcom_categories: [{ category: "profile-after-change" }],
  QueryInterface: XPCOMUtils.generateQI([])
};

function NSGetModule(compMgr, fileSpec)
  XPCOMUtils.generateModule([UpdateServiceStub]);
