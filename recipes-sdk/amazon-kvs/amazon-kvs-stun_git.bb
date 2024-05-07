SUMMARY = "Amazon Kinesis Video Streams SDP"
DESCRIPTION = "The goal of the SDP library is provide SDP Serializer and Deserializer functionalities."
HOMEPAGE = "https://github.com/awslabs/amazon-kinesis-video-streams-stun"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=34400b68072d710fecd0a2940a0d1658"
BRANCH ?= "main"
SRC_URI = "\
    git://github.com/awslabs/amazon-kinesis-video-streams-stun.git;protocol=https;branch=${BRANCH} \
    "

# this recipe should be released only together with amazon-kvs-webrtc-sdk
UPSTREAM_VERSION_UNKNOWN = "1"
# set to match only git_invalid_tag_regex because UPSTREAM_VERSION_UNKNOWN seems to be broken for git
UPSTREAM_CHECK_GITTAGREGEX = "git_invalid_tag_regex"
# this SRCREV commit id should not different than this:
# https://github.com/awslabs/amazon-kinesis-video-streams-webrtc-sdk-c/blob/develop/CMake/Dependencies/libkvsstun-CMakeLists.txt
SRCREV = "2fa9ed638239aa058a71db0d9430ab427e6e21a3"

S = "${WORKDIR}/git"

inherit cmake

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

# enable PACKAGECONFIG = "static" to build static instead of shared libs
PACKAGECONFIG[static] = "-DBUILD_SHARED_LIBS=OFF,-DBUILD_SHARED_LIBS=ON,"

EXTRA_OECMAKE += "\
    -DCMAKE_C_FLAGS=-DSDP_DO_NOT_USE_CUSTOM_CONFIG \
"
