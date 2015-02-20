package net.sumppen.whatsapi4j;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;

public class WhatsApi4jTest {

	private final Logger log = Logger.getLogger(WhatsApi4jTest.class);
	@Test
	public void testPbkdf2() throws Exception {
        WhatsApi whatsapi = new WhatsApi("12345678", "", "test");
        byte[] key = whatsapi.pbkdf2("PBKDF2WithHmacSHA1", "test".getBytes(), "1234".getBytes(), 16, 20, false);
        log.debug(new String(key));
        assertTrue("35e3945d26a46d5cb76e986e4ac305b857b9a56b".equals(new String(key)));
	}
	
//	@Test
//	public void testKeyStream() throws Exception {
//		String key = "key";
//		KeyStream in = new KeyStream(key.getBytes());
//		KeyStream out = new KeyStream(key.getBytes());
//		String data = "foo";
//		byte[] encoded = in.encode(data.getBytes(), 0, data.length(),false);
//		log.debug(WhatsApi.toHex(encoded));
//		byte[] decoded = out.decode(encoded, 0, encoded.length);
//		log.debug(new String(decoded));
//		assertTrue(data.equals(new String(decoded)));
//	}
	
	@Test
	public void testGenerateKeys() throws Exception {
		String challenge = "73b82585b1a15cf3687501355ea2ba3f5e0cfe9c";
        WhatsApi whatsapi = new WhatsApi("12345678", "", "test");
        whatsapi.setChallengeData(challenge);
        whatsapi.setPassword("nb97ZaxkMrX5nNDuEb8H2o4SBJI=");
        List<byte[]> keys = whatsapi.generateKeys();
        assertEquals("251c1d9325bfd34025e6e2efb27e0df8fc334262", ProtocolNode.bin2hex(keys.get(0)));
        assertEquals("bde430475e6bf63966b4d3baf39f4143fbddb3a2", ProtocolNode.bin2hex(keys.get(1)));
        assertEquals("282469087550e82e04ccdc6d41a10842a731621f", ProtocolNode.bin2hex(keys.get(2)));
        assertEquals("631f85abed3f057098ee89a4d6b335d8ec53a9d0", ProtocolNode.bin2hex(keys.get(3)));
	}
	
	@Test
	public void testHashHMAC() throws Exception {
		KeyStream ks = new KeyStream(WhatsApi.hex2bin("57228438fcf09b18c8ad28428d39a7c9ae8fa36c"), WhatsApi.hex2bin("691760f81e2d40e71b6888f6b9210f3207073e60"));
		byte[] data = WhatsApi.hex2bin("000b6da46516831d0dd2eb4a1f1ca053939a4640c150bb3cb7a6c7ad14cd");
		byte[] res = ks.computeMac(data, 0, data.length);
		assertEquals("78f016fc1e8331b24434dfe8a177e82a0ee69175", ProtocolNode.bin2hex(res));
	}
	
	@Test
	public void testChallengeResponse() throws Exception {
		String challenge = "b32e685dd1b680dd01fa5e23cd37a25f59c0953b";
        WhatsApi whatsapi = new WhatsApi("6583206450", "", "test");
        whatsapi.setChallengeData(challenge);
        whatsapi.setPassword("nb97ZaxkMrX5nNDuEb8H2o4SBJI=");
        byte[] resp = whatsapi.authenticate();
        assertEquals("4a2702c33c66deed719791724635de98f63fbca4ee859288b01b367703a71cb1dcf6", ProtocolNode.bin2hex(resp));
	}
	
	@Test
	public void testChallengeResponse2() throws Exception {
		String challenge = "2fee792594a69d1790163b46e7b9fcd7a25d3766";
        WhatsApi whatsapi = new WhatsApi("6583206450", "", "test");
        whatsapi.setChallengeData(challenge);
        whatsapi.setPassword("nb97ZaxkMrX5nNDuEb8H2o4SBJI=");
        byte[] resp = whatsapi.authenticate();
        assertEquals("5c3babbbcad22dd3505c1cd7f04c488dd34b4f33c8aba6bb83ce1976e5a5aba43bae", ProtocolNode.bin2hex(resp));
	}
	
	@Test
	public void testBase64Decode() throws Exception {
		String password = "+pRhR5WH/tt4pNG5uO+rkNqRPh4=";
        WhatsApi whatsapi = new WhatsApi("12345678", "", "test");
        byte[] resp = whatsapi.base64_decode(password);
        String str = WhatsApi.toHex(resp);
        log.debug(str);
        assertTrue(str.equals("fa9461479587fedb78a4d1b9b8efab90da913e1e"));
	}

	@Test
	public void testReadCountries() throws Exception {
        WhatsApi whatsapi = new WhatsApi("12345678", "", "test");
        List<Country> countries = whatsapi.getCountries();
        for(Country country : countries) {
        	log.debug(country);
        }
        assertEquals(254,countries.size());
	}
	
	@Test
	public void testGenerateRequestToken() throws Exception {
        WhatsApi whatsapi = new WhatsApi("12345678", "", "test");
        String token = whatsapi.generateRequestToken("Finland", "401122333");
//        assertEquals("vMg5esCVsZdT2auKeWtoYLWEcCY=", token);
        assertEquals("c18f993c24121c83e84aafb8a687117e", token);
	}

	@Test
	public void testCheckIdentity() throws Exception {
        WhatsApi whatsapi = new WhatsApi("12345678", "", "test");
        assertFalse(whatsapi.checkIdentity("e807f1fcf82d132f9bb018ca6738a19f"));
        String id = whatsapi.buildIdentity("e807f1fcf82d132f9bb018ca6738a19f");
        assertTrue(whatsapi.checkIdentity(id));
        assertEquals("%b1%02%ce%1d%5e%eb%ac%2bmt%bd%a8%c8%7cg%a0p%c8%04%91",id);
	}
	
	@Test
	public void testDecodeData() throws Exception {
		String challenge = "59ea12d664fb4aa4848c55ffa7ded07d65217450";
        WhatsApi whatsapi = new WhatsApi("123456789", "", "test");
        whatsapi.setChallengeData(challenge);
        whatsapi.setPassword("nb97ZaxkMrX5nNDuEb8H2o4SBJI=");
        byte[] resp = whatsapi.authenticate();
        KeyStream key = whatsapi.getInputKey();
		String buffer = "2db11e924b22cd0a762a9903c9302fbd7d4fd0c8a98e276736c0f2c951202db5290e23159f423cdad8fbe954e1fa4015338a008b7b3c3c7ef67188da47";
		byte[] b = WhatsApi.hex2bin(buffer);
		byte[] d = key.decode(b, b.length-4, 0, b.length-4);
		String decoded = "f80e9fa1ff0514241837137cfc013451399a061fff0514235394222eff051455075422fc147edf37f0a8d519c041e476d97d1349e861509ddc7188da47";
		assertEquals(decoded, ProtocolNode.bin2hex(d));
	}

	@Test
	public void testEncryptedReader() throws Exception {
		String challenge = "197f119ef43d25a6b38a09dff59329a9548b5475";
        WhatsApi whatsapi = new WhatsApi("123456789", "", "test");
        whatsapi.setChallengeData(challenge);
        whatsapi.setPassword("nb97ZaxkMrX5nNDuEb8H2o4SBJI=");
        whatsapi.authenticate();
        KeyStream key = whatsapi.getInputKey();
        BinTreeNodeReader reader = new BinTreeNodeReader();
        reader.setKey(key);
		String buffer = "80003db8479ddd1e84163b29871735d6bfe4297e71db35185b40f40bbe581770e10947f7f31b0a8df357abaac289e21152b9e8db209936ca2dd4d3bcbcba0d60";
		byte[] b = WhatsApi.hex2bin(buffer);
		ProtocolNode node = reader.nextTree(b);
		log.debug(node.toString());
		assertEquals("active", node.getAttribute("status"));
		assertEquals("1424242627", node.getAttribute("t"));
		assertEquals("4", node.getAttribute("props"));
		assertEquals("free", node.getAttribute("kind"));
		assertEquals("1423539422", node.getAttribute("creation"));
	}
	
	@Test
	public void testEncryptedWriter() throws Exception {
		String challenge = "17a92a95be51f28b8eb5832f31b974207b22ff60";
        WhatsApi whatsapi = new WhatsApi("123456789", "", "test");
        whatsapi.setChallengeData(challenge);
        whatsapi.setPassword("nb97ZaxkMrX5nNDuEb8H2o4SBJI=");
        whatsapi.authenticate();
        KeyStream key = whatsapi.getOutputKey();
        BinTreeNodeWriter writer = new BinTreeNodeWriter();
        writer.setKey(key);
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("name", "Test Account");
		ProtocolNode node = new ProtocolNode("presence", attributes, null, null);
        byte[] data = writer.write(node, true);
        assertEquals("80001622ef2300f48212f0b22cc1503e6f10493558c79ef30d", ProtocolNode.bin2hex(data));
	}



}
