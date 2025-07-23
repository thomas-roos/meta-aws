FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://004-fix-usrsctp-cmake-version.patch \
    file://007-use-system-libsrtp-direct.patch \
"

# Add the CMake policy version minimum to fix the usrsctp build
# Explicitly disable tests and benchmarks
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5 -DBUILD_TEST=OFF -DBUILD_BENCHMARK=OFF -DENABLE_AWS_SDK_IN_TESTS=OFF -DBUILD_DEPENDENCIES=OFF"

# Add libsrtp and amazon-kvs-producer-sdk-c as dependencies
DEPENDS += "libsrtp amazon-kvs-producer-sdk-c"

# Ignore fuzz in patches
PATCHLEVEL = "-p1 -F 3"
