SUMMARY = "Amazon Kinesis Video Streams WebRTC"
DESCRIPTION = "Pure C WebRTC Client for Amazon Kinesis Video Streams"
HOMEPAGE = "https://github.com/awslabs/amazon-kinesis-video-streams-webrtc-sdk-c"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"

DEPENDS += "\
    aws-sdk-cpp \
    amazon-kvs-producer-sdk-c \
    amazon-kvs-rtp \
    amazon-kvs-sdp \
    amazon-kvs-stun \
    curl \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    libsrtp \
    libwebsockets \
    openssl \
    usrsctp \
    mbedtls \
"

PROVIDES += "aws/amazon-kvs-webrtc-sdk"

BRANCH = "ERROR"
SRC_URI = "\
    git://github.com/awslabs/amazon-kinesis-video-streams-webrtc-sdk-c.git;protocol=https;branch=${BRANCH} \
    file://run-ptest \
    file://ptest_result.py \
"

SRCREV = "f1073439213db4aa4b4b7cc1423609d920dd5489"

S = "${WORKDIR}/git"

inherit cmake pkgconfig ptest

PACKAGECONFIG ??= "\
    ${@bb.utils.contains('PTEST_ENABLED', '1', 'with-tests with-samples', '', d)} \
    "

PACKAGECONFIG[with-tests] = "-DBUILD_TEST=ON,-DBUILD_TEST=OFF,gtest"

PACKAGECONFIG[with-samples] = "-DBUILD_SAMPLE=ON ,-DBUILD_SAMPLE=OFF,"

# enable PACKAGECONFIG = "static" to build static instead of shared libs
PACKAGECONFIG[static] = "-DBUILD_SHARED_LIBS=OFF,-DBUILD_SHARED_LIBS=ON,"

do_configure[network] = "1"

do_install:append () {
    install ${B}/libkvsWebRtcThreadpool.so ${D}${libdir}
}

FILES:${PN} += "\
    ${libdir} \
    "

CFLAGS:append = " -Wl,-Bsymbolic"
CFLAGS:append = " -I${S}/configs"

EXTRA_OECMAKE += "\
    -DBUILD_DEPENDENCIES=OFF \
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

do_install_ptest () {
    cp -r ${B}/tst/webrtc_client_test ${D}${PTEST_PATH}/
    install -m 0755 ${WORKDIR}/ptest_result.py ${D}${PTEST_PATH}/
}

# nooelint: oelint.vars.insaneskip:INSANE_SKIP
INSANE_SKIP:${PN}-ptest += "expanded-d"
