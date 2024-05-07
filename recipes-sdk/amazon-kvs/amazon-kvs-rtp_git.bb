SUMMARY = "Amazon Kinesis Video Streams RTP"
DESCRIPTION = "The goal of the Real-time Transport Protocol (RTP) library is to provide RTP Serializer and Deserializer functionalities. Along with this, RTP library also provide codec packetization and depacketization functionality for G.711, VP8, Opus and H.264 codecs."
HOMEPAGE = "https://github.com/awslabs/amazon-kinesis-video-streams-rtp"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"
BRANCH ?= "main"

SRC_URI = "git://github.com/moninom1/amazon-kinesis-video-streams-rtp.git;protocol=https;branch=${BRANCH}"

# this recipe should be released only together with amazon-kvs-webrtc-sdk
UPSTREAM_VERSION_UNKNOWN = "1"
# set to match only git_invalid_tag_regex because UPSTREAM_VERSION_UNKNOWN seems to be broken for git
UPSTREAM_CHECK_GITTAGREGEX = "git_invalid_tag_regex"
# this SRCREV commit id should not different than this:
# https://github.com/awslabs/amazon-kinesis-video-streams-webrtc-sdk-c/blob/rtpWrapper/CMake/Dependencies/libkvsrtp-CMakeLists.txt
SRCREV = "ee2b4edf92f0ca62788c004dc1dee3e0a42ebf69"
# SRCREV = "014a1c9d497d88d020d72f73cb38154997e1ef18"

S = "${WORKDIR}/git"

inherit cmake

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

# enable PACKAGECONFIG = "static" to build static instead of shared libs
PACKAGECONFIG[static] = "-DBUILD_SHARED_LIBS=OFF,-DBUILD_SHARED_LIBS=ON,"

EXTRA_OECMAKE += "\
    -DCMAKE_C_FLAGS=-DSDP_DO_NOT_USE_CUSTOM_CONFIG \
"
