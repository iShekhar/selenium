package org.openqa.selenium.qtwebkit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.QtWebkitAugmenter;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HTML5AudioTagTest extends JUnit4TestBase {
    @Before
    public void setUp() throws Exception {
        driver.get(pages.html5AudioTest);
    }

    @AfterClass
    public static void cleanUpDriver() {
        JUnit4TestBase.removeDriver();
    }

    @Test
    public void testRemotePlayerState() {
        WebElement element = driver.findElement(By.id("audioPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
        player.setState(Player.PlayerState.playing);
        Player.PlayerState state = player.getState();
        assertEquals(Player.PlayerState.playing, state);
        assertEquals(null, element.getAttribute("paused"));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }

        player.setState(Player.PlayerState.paused);
        state = player.getState();
        assertEquals(Player.PlayerState.paused, state);
        assertEquals("true", element.getAttribute("paused"));

        player.setState(Player.PlayerState.stopped);
        state = player.getState();
        assertEquals("true", element.getAttribute("paused"));
        assertEquals(0, player.getCurrentPlayingPosition(), 0.01);
        assertEquals(Player.PlayerState.stopped, state);
    }

    @Test
    public void testRemotePlayerSetMute() {
        WebElement element = driver.findElement(By.id("audioPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
        player.setMute(true);
        assertEquals("true", element.getAttribute("muted"));

        player.setMute(false);
        assertEquals(null, element.getAttribute("muted"));
    }

    @Test
    public void testRemotePlayerGetMute() {
        WebElement element = driver.findElement(By.id("audioPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
        assertEquals(player.isMuted() ? "true" : null, element.getAttribute("muted"));
        player.setMute(true);
        assertEquals(player.isMuted() ? "true" : null, element.getAttribute("muted"));
    }

    @Test
    public void testRemotePlayerSeek() {
        WebElement element = driver.findElement(By.id("audioPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
        assertEquals(player.getCurrentPlayingPosition(), 0, 0.1);

        player.setState(Player.PlayerState.playing);
        assertNotEquals(player.getCurrentPlayingPosition(), 0);
    }

    @Test
    public void testRemotePlayerVolume() {
        WebElement element = driver.findElement(By.id("audioPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
        player.setVolume(0.5);
        assertEquals(0.5, player.getVolume(), 0);
        assertEquals("0.5", element.getAttribute("volume"));

        player.setVolume(0);
        assertEquals(0, player.getVolume(), 0);
        assertEquals("0", element.getAttribute("volume"));

        player.setVolume(1.0);
        assertEquals(1.0, player.getVolume(), 0);
        assertEquals("1", element.getAttribute("volume"));
    }

    @Test
    public void testRemotePlayerVolumeAndMute() {
        WebElement element = driver.findElement(By.id("audioPlayer"));
        Player player = getPlayer((RemoteWebElement) element);
        player.setVolume(0.5);
        player.setMute(true);
        player.setMute(false);
        assertEquals(0.5, player.getVolume(), 0.02);
        assertEquals("0.5", element.getAttribute("volume"));
    }

    public Player getPlayer(RemoteWebElement element) {
        return (Player) (new QtWebkitAugmenter().augment(element));
    }

}
