%{!?_idehome:  %define _idehome  /usr/local/redhawk/ide}
%define _prefix %{_idehome}
%define debug_package %{nil}
%define __os_install_post %{nil}


Name:           redhawk-ide
Summary:        REDHAWK Integrated Developer Environment
Version:        1.10.0
Release:        1%{?dist}
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}-buildroot
Group:          Applications/Engineering
License:        Eclipse Public License (EPL)
URL:            http://redhawksdr.org/
Source:         %{name}-%{version}.tar.gz
Vendor:         REDHAWK

Requires:       java-devel >= 1.6
AutoReqProv:    no


%description
REDHAWK Integrated Developer Environment
 * Commit: __REVISION__
 * Source Date/Time: __DATETIME__

%prep
%setup

%install
mkdir -p $RPM_BUILD_ROOT%{_prefix}/%{version}
cp -r * $RPM_BUILD_ROOT%{_prefix}/%{version}
jacorb_dir=`find %{_prefix}/%{version}/plugins -name org.jacorb_*`
echo "java.endorsed.dirs=$jacorb_dir" >> $RPM_BUILD_ROOT%{_prefix}/%{version}/eclipse.ini

%clean
rm -rf $RPM_BUILD_ROOT

%files
%defattr(-, root, root)
%{_prefix}/%{version}

%post
%{_prefix}/%{version}/eclipse -nosplash -consolelog -initialize
