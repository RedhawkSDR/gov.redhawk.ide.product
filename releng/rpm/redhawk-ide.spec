%{!?_idehome:  %define _idehome  /usr/local/redhawk/ide}
%define debug_package %{nil}
%define __os_install_post %{nil}


Name:           redhawk-ide
Summary:        REDHAWK Integrated Developer Environment
Version:        1.10.0
Release:        2%{?dist}
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}-buildroot
Group:          Applications/Engineering
License:        Eclipse Public License (EPL)
URL:            http://redhawksdr.org/
Source0:        %{name}-%{version}.tar.gz
Source1:        redhawk.desktop
Vendor:         REDHAWK

BuildRequires:  desktop-file-utils
Requires:       java-devel >= 1.6
Requires:       jacorb >= 3.3.0
Requires:       redhawk >= 1.10
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
desktop-file-install --dir=$RPM_BUILD_ROOT%{_datadir}/applications %{SOURCE1}
install -m 644 features/gov.redhawk.sdk_*/icon.xpm %{_datadir}/icons/hicolor/128x128/apps/redhawk.xpm


%clean
rm -rf $RPM_BUILD_ROOT


%files
%defattr(-, root, root)
%{_idehome}/%{version}
%{_bindir}/rhide
%{_datadir}/icons/hicolor/128x128/apps/redhawk.xpm
%{_datadir}/applications/redhawk.desktop


%post
%{_idehome}/%{version}/eclipse -nosplash -consolelog -initialize
/bin/touch --no-create %{_datadir}/icons/hicolor &>/dev/null || :


%postun
if [ $1 -eq 0 ] ; then
    /bin/touch --no-create %{_datadir}/icons/hicolor &>/dev/null
    /usr/bin/gtk-update-icon-cache %{_datadir}/icons/hicolor &>/dev/null || :
fi


%posttrans
/usr/bin/gtk-update-icon-cache %{_datadir}/icons/hicolor &>/dev/null || :

