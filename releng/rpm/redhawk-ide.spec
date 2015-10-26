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
%{!?_idehome:  %define _idehome  /usr/local/redhawk/ide}
%define debug_package %{nil}
%define __os_install_post %{nil}


Name:           redhawk-ide
Summary:        REDHAWK Integrated Developer Environment
Version:        1.8.10
Release:        1%{?dist}
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}-buildroot
Group:          Applications/Engineering
License:        Eclipse Public License (EPL)
URL:            http://redhawksdr.org/
Source:         %{name}-%{version}.zip
Vendor:         REDHAWK

Requires:       java-devel >= 1.6
Requires:       redhawk >= 1.8
AutoReqProv:    no


%description
REDHAWK Integrated Developer Environment
 * Commit: __REVISION__
 * Source Date/Time: __DATETIME__


%prep
%setup -q


%install
mkdir -p $RPM_BUILD_ROOT%{_idehome}/%{version}
mkdir -p $RPM_BUILD_ROOT%{_bindir}
cp -r * $RPM_BUILD_ROOT%{_idehome}/%{version}
ln -s %{_idehome}/%{version}/eclipse $RPM_BUILD_ROOT%{_bindir}/rhide


%clean
rm -rf $RPM_BUILD_ROOT


%files
%defattr(-, root, root)
%{_idehome}/%{version}
%{_bindir}/rhide

