# - Try to find libsrtp
# Once done this will define
#  SRTP_FOUND - System has libsrtp
#  SRTP_INCLUDE_DIRS - The libsrtp include directories
#  SRTP_LIBRARIES - The libraries needed to use libsrtp

find_package(PkgConfig QUIET)
pkg_check_modules(PC_SRTP QUIET libsrtp2)

find_path(SRTP_INCLUDE_DIR
          NAMES srtp.h
          HINTS ${PC_SRTP_INCLUDEDIR} ${PC_SRTP_INCLUDE_DIRS})

find_library(SRTP_LIBRARY
             NAMES srtp2
             HINTS ${PC_SRTP_LIBDIR} ${PC_SRTP_LIBRARY_DIRS})

include(FindPackageHandleStandardArgs)
find_package_handle_standard_args(SRTP DEFAULT_MSG SRTP_LIBRARY SRTP_INCLUDE_DIR)

mark_as_advanced(SRTP_INCLUDE_DIR SRTP_LIBRARY)

set(SRTP_LIBRARIES ${SRTP_LIBRARY})
set(SRTP_INCLUDE_DIRS ${SRTP_INCLUDE_DIR})
