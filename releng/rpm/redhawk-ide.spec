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
Version:        2.1.2
Release:        3%{?dist}
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}-buildroot
Group:          Applications/Engineering
License:        Eclipse Public License (EPL)
URL:            http://redhawksdr.org/
Source0:        %{name}-%{version}.tar.gz
Source1:        redhawk.desktop
Vendor:         REDHAWK

BuildRequires:  desktop-file-utils
Requires:       java-1.8.0-openjdk-devel
%if 0%{?rhel} >= 7 || 0%{?fedora} >= 17
Requires:       PackageKit-gtk3-module libcanberra-gtk3 libwebkit2gtk
%else
Requires:       PackageKit-gtk-module libcanberra-gtk2 webkitgtk
%endif
Requires:       redhawk-devel >= 2.0
Requires:       redhawk-codegen >= 2.0
Requires:       bulkioInterfaces >= 2.0
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
mkdir -p $RPM_BUILD_ROOT%{_datadir}/applications
mkdir -p $RPM_BUILD_ROOT%{_datadir}/icons/hicolor/128x128/apps
desktop-file-install --dir=$RPM_BUILD_ROOT%{_datadir}/applications %{SOURCE1} --vendor=
install -m 644 features/gov.redhawk.sdk_*/icon.xpm $RPM_BUILD_ROOT%{_datadir}/icons/hicolor/128x128/apps/redhawk.xpm


%clean
rm -rf $RPM_BUILD_ROOT


%files
%defattr(-, root, root)
%{_idehome}/%{version}
%{_bindir}/rhide
%{_datadir}/icons/hicolor/128x128/apps/redhawk.xpm
%{_datadir}/applications/redhawk.desktop


%post
#%{_idehome}/%{version}/eclipse -nosplash -consolelog -initialize
/bin/touch --no-create %{_datadir}/icons/hicolor &>/dev/null || :


%postun
if [ $1 -eq 0 ] ; then
    /bin/touch --no-create %{_datadir}/icons/hicolor &>/dev/null
    /usr/bin/gtk-update-icon-cache %{_datadir}/icons/hicolor &>/dev/null || :
fi


%posttrans
/usr/bin/gtk-update-icon-cache %{_datadir}/icons/hicolor &>/dev/null || :

