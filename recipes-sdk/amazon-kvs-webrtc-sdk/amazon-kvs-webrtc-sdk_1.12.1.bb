SUMMARY = "Amazon Kinesis Video Streams WebRTC"
DESCRIPTION = "Pure C WebRTC Client for Amazon Kinesis Video Streams"
HOMEPAGE = "https://github.com/awslabs/amazon-kinesis-video-streams-webrtc-sdk-c"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"

DEPENDS += "\
    curl \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    openssl \
    libsrtp \
    ${@bb.utils.contains('PACKAGECONFIG', 'no-buildin-deps', 'aws-sdk-cpp amazon-kvs-producer-sdk-c libwebsockets usrsctp', '', d)} \
"

PROVIDES += "aws/amazon-kvs-webrtc-sdk"

BRANCH = "main"
SRC_URI = "\
    git://github.com/awslabs/amazon-kinesis-video-streams-webrtc-sdk-c.git;protocol=https;branch=${BRANCH} \
    ${@bb.utils.contains('PACKAGECONFIG', 'no-buildin-deps', '', 'git://github.com/awslabs/amazon-kinesis-video-streams-producer-c.git;protocol=https;name=kvscommon;destsuffix=open-source/amazon-kinesis-video-streams-producer-c;nobranch=1', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'no-buildin-deps', '', 'git://github.com/sctplab/usrsctp.git;protocol=https;name=usrsctp;destsuffix=open-source/usrsctp;nobranch=1', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'no-buildin-deps', '', 'git://github.com/warmcat/libwebsockets.git;protocol=https;name=websockets;destsuffix=open-source/libwebsockets;nobranch=1', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'no-buildin-deps', '', 'git://github.com/ARMmbed/mbedtls.git;protocol=https;name=mbedtls;destsuffix=open-source/mbedtls;nobranch=1', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'no-buildin-deps', '', 'git://github.com/zserge/jsmn.git;protocol=https;name=jsmn;destsuffix=open-source/jsmn;nobranch=1', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'no-buildin-deps', '', 'git://github.com/awslabs/amazon-kinesis-video-streams-producer-c.git;protocol=https;name=kvscommon;destsuffix=open-source/amazon-kinesis-video-streams-producer-c;nobranch=1', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'with-tests', 'git://github.com/google/googletest.git;protocol=https;name=gtest;destsuffix=open-source/googletest;nobranch=1', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'with-aws-sdk-tests', 'git://github.com/aws/aws-sdk-cpp.git;protocol=https;name=awscpp;destsuffix=open-source/aws-sdk-cpp;nobranch=1', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'with-benchmark', '', 'git://github.com/google/benchmark.git;protocol=https;name=benchmark;destsuffix=open-source/benchmark;nobranch=1', d)} \
    file://run-ptest \
    file://ptest_result.py \
    ${@bb.utils.contains('PACKAGECONFIG', 'no-buildin-deps', '', 'file://001-use-local-source-dir.patch', d)} \
    file://002-skip-unnecessary-patches.patch \
    file://003-disable-minimal-examples.patch \
"


SRCREV = "cf817bc5d18f3e4bd499c6b0f9a68c6f4d7e01de"

# must match CMake/Dependencies files (check is done through failing patch)
SRCREV_usrsctp = "1ade45cbadfd19298d2c47dc538962d4425ad2dd"
# v4.3.3
SRCREV_websockets = "4415e84c095857629863804e941b9e1c2e9347ef"
# v2.28.8
SRCREV_mbedtls = "19cd99c38606b827bafa99bc9b790e78476a4b95"
# v1.0.0
SRCREV_jsmn = "a0ca81fe76f5057c08ad3640cd39afbc03700025"
# v1.5.4
SRCREV_kvscommon = "5fb356e484d734ce3650c751a4469985c488bc1f"
# release-1.12.1
SRCREV_gtest = "58d77fa8070e8cec2dc1ed015d66b454c8d78850"
# 1.11.217
SRCREV_awscpp = "63d8e4f1b31b9c10e4252206e0cc78d247991caf"
# v1.5.1
SRCREV_benchmark = "db8e93989303de2fc8faf0e2084c3aba1627775b"

SRCREV_FORMAT .= "_usrsctp_websockets_mbedtls_jsmn_kvscommon_gtest_awscpp_benchmark"

inherit cmake pkgconfig ptest

#PACKAGECONFIG ??= "\
#    ${@bb.utils.contains('PTEST_ENABLED', '1', 'with-tests with-samples', '', d)} \
#    "

PACKAGECONFIG[with-tests] = "-DBUILD_TEST=ON,-DBUILD_TEST=OFF,gtest"

PACKAGECONFIG[with-samples] = "-DBUILD_SAMPLE=ON ,-DBUILD_SAMPLE=OFF,"

# Default is to build static libraries, use shared to build shared libraries
PACKAGECONFIG[shared] = "-DBUILD_SHARED_LIBS=ON,-DBUILD_SHARED_LIBS=OFF,"

# no-buildin-deps - use system dependencies instead of building from source
PACKAGECONFIG[no-buildin-deps] = "-DBUILD_DEPENDENCIES=OFF,-DBUILD_DEPENDENCIES=ON -DBUILD_OPENSSL_PLATFORM=${arch},,"

# with-aws-sdk-tests - enable AWS SDK in tests (requires awscpp dependency)
PACKAGECONFIG[with-aws-sdk-tests] = "-DENABLE_AWS_SDK_IN_TESTS=ON,-DENABLE_AWS_SDK_IN_TESTS=OFF,,"

# with-benchmark - enable benchmark building
PACKAGECONFIG[with-benchmark] = "-DBUILD_BENCHMARK=ON,-DBUILD_BENCHMARK=OFF,,"

# Network access should be enabled for the configure step
do_configure[network] = "1"

FILES:${PN} += "\
    ${@bb.utils.contains('PACKAGECONFIG', 'with-samples', '/samples/*', '', d)} \
    ${libdir} \
    "

CFLAGS:append = " -Wl,-Bsymbolic"

# arm32 - gives this warning
# nooelint: oelint.vars.specific
CFLAGS:append:arm = " -Wno-incompatible-pointer-types"

EXTRA_OECMAKE += "\
    -DCODE_COVERAGE=OFF \
    -DADDRESS_SANITIZER=OFF \
    -DMEMORY_SANITIZER=OFF \
    -DTHREAD_SANITIZER=OFF \
    -DUNDEFINED_BEHAVIOR_SANITIZER=OFF \
    -DDEBUG_HEAP=OFF \
    -DCOMPILER_WARNINGS=OFF \
    -DALIGNED_MEMORY_MODEL=OFF \
    -DCMAKE_POLICY_VERSION_MINIMUM=3.5 \
"

FILES_SOLIBSDEV = ""

RDEPENDS:${PN}-ptest += "\
    bash \
    python3 \
    "

BBCLASSEXTEND = "native nativesdk"

# fix DSO missing from command line
LDFLAGS += "-Wl,--copy-dt-needed-entries"

# fix package neo-ai-tvm contains bad RPATH
EXTRA_OECMAKE += "-DCMAKE_SKIP_RPATH=1"

do_install:append () {
    if [ -n "${@bb.utils.contains('PACKAGECONFIG', 'with-samples', '1', '', d)}" ]; then
        install -d ${D}/samples/
        cp -r ${S}/samples/h264SampleFrames ${D}/samples/
        cp -r ${S}/samples/h265SampleFrames ${D}/samples/
        cp -r ${S}/samples/opusSampleFrames ${D}/samples/
    fi
}

do_install_ptest () {
    cp -r ${B}/tst/webrtc_client_test ${D}${PTEST_PATH}/
    install -m 0755 ${UNPACKDIR}/ptest_result.py ${D}${PTEST_PATH}/
}

# nooelint: oelint.vars.insaneskip:INSANE_SKIP
INSANE_SKIP:${PN}-ptest += "buildpaths"

# nooelint: oelint.vars.insaneskip:INSANE_SKIP
INSANE_SKIP:${PN} += "buildpaths"

# nooelint: oelint.vars.insaneskip:INSANE_SKIP
INSANE_SKIP:${PN}-dbg += "buildpaths"
