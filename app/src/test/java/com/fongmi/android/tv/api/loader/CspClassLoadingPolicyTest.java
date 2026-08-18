package com.fongmi.android.tv.api.loader;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CspClassLoadingPolicyTest {

    @Test
    public void protobufClassesAreChildFirst() {
        assertTrue(CspClassLoadingPolicy.isChildFirst("com.google.protobuf.Internal"));
        assertTrue(CspClassLoadingPolicy.isChildFirst("com.google.protobuf.SingleFieldBuilder"));
    }

    @Test
    public void platformAndHostClassesRemainParentFirst() {
        assertFalse(CspClassLoadingPolicy.isChildFirst("java.lang.String"));
        assertFalse(CspClassLoadingPolicy.isChildFirst("android.content.Context"));
        assertFalse(CspClassLoadingPolicy.isChildFirst("com.fongmi.android.tv.App"));
        assertFalse(CspClassLoadingPolicy.isChildFirst("com.github.catvod.crawler.Spider"));
        assertFalse(CspClassLoadingPolicy.isChildFirst(null));
    }
}
