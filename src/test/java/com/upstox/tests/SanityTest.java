package com.upstox.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SanityTest {

  @Test
  public void sanity_shouldPass() {
    Assert.assertTrue(true, "Sanity test failed (it shouldn't).");
  }
}