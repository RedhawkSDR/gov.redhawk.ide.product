%{!?_idehome:  %define _idehome  /usr/local/redhawk/ide}
%define _prefix %{_idehome}
%define debug_package %{nil}
%define __os_install_post %{nil}


Name:           redhawk-ide
Summary:        REDHAWK Integrated Developer Environment
Version:        1.9.1
Release:        1%{?dist}
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}-buildroot
Group:          Applications/Engineering
License:        Eclipse Public License (EPL)
URL:            http://redhawksdr.org/
Source:         %{name}-%{version}.zip
Vendor:         REDHAWK

Requires:       java-devel >= 1.6
Requires:       jacorb >= 3.3.0
Requires:       redhawk >= 1.9
AutoReqProv:    no


%description
REDHAWK Integrated Developer Environment
 * Commit: __REVISION__
 * Source Date/Time: __DATETIME__

%prep
%setup

%install
mkdir -p $RPM_BUILD_ROOT%{_prefix}/%{version}
mkdir -p $RPM_BUILD_ROOT/usr/bin
cp -r * $RPM_BUILD_ROOT%{_prefix}/%{version}
cd $RPM_BUILD_ROOT
ln -s %{_prefix}/%{version}/eclipse $RPM_BUILD_ROOT/usr/bin/rhide

%clean
rm -rf $RPM_BUILD_ROOT

%files
%defattr(-, root, root)
%{_prefix}/%{version}
/usr/bin/rhide

%post
%{_prefix}/%{version}/eclipse -nosplash -consolelog -initialize > /dev/null 2>&1
