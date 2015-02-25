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

	@Test
	public void testParseLocation() throws Exception {
		String locationMessage = "000945f80c593afaff063584077181089144ff071424659087a359a757a1ff05142486350366fc0c4b696d204c696e6462657267f801f80a57a7ec42ec44fc0732342e36393935ec3efc0736302e313637342b7efd0008ecffd8ffe000104a46494600010100004800480000ffe100584578696600004d4d002a000000080002011200030000000100010000876900040000000100000026000000000003a00100030000000100010000a00200040000000100000064a0030004000000010000006400000000ffed003850686f746f73686f7020332e30003842494d04040000000000003842494d0425000000000010d41d8cd98f00b204e9800998ecf8427effc00011080064006403012200021101031101ffc4001f0000010501010101010100000000000000000102030405060708090a0bffc400b5100002010303020403050504040000017d01020300041105122131410613516107227114328191a1082342b1c11552d1f02433627282090a161718191a25262728292a3435363738393a434445464748494a535455565758595a636465666768696a737475767778797a838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae1e2e3e4e5e6e7e8e9eaf1f2f3f4f5f6f7f8f9faffc4001f0100030101010101010101010000000000000102030405060708090a0bffc400b51100020102040403040705040400010277000102031104052131061241510761711322328108144291a1b1c109233352f0156272d10a162434e125f11718191a262728292a35363738393a434445464748494a535455565758595a636465666768696a737475767778797a82838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae2e3e4e5e6e7e8e9eaf2f3f4f5f6f7f8f9faffdb0043000606060606060a06060a0e0a0a0a0e120e0e0e0e12171212121212171c1717171717171c1c1c1c1c1c1c1c22222222222227272727272c2c2c2c2c2c2c2c2c2cffdb0043010707070b0a0b130a0a132e1f1a1f2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2effdd00040007ffda000c03010002110311003f00fa728aa88cd03ac321dc8dfeadfaff00c04fbfa1eff5eb6eb9a9d4535746b28d828a28ab2428e9cd1450073b736d369db25b69084893626f1bf6824111a2ae0b16200c92481ef935b1697715da128ca5d30b22ab06d8d8e5491e9561d564468dbeeb020f24707dc73581710cb67705a319474f2d427c9f28ce235c1f902fde6909fa0aadc5b1d0d359822966e82aa595f437b6ff00688dc32e5b9c6dc81df04938c11cf7ebd08a7e5a57e38c723fd91ebf53dbd3ad2b0c00695ce78ec7d87a0f7f53f855900018142a8501578029690051451401ffd0fa38c6a87ecafc4527dc3fdd6eb81fcc54d048cc0c72ff00ac4e1bdfd08f63524b1aca8636efdc7507b11f4aa86460be7b0ccb0fcb263baf5c8fe63f115e6497b19dd6dfd7e5d3e6752f7d58bf452020804720d2d769885735e27d69b46b38cc25566b890468cff7533d58fb0ae96b97f15e82faf69c2180859a26de99e87b107eb59d6e6e47c9b9d997fb1fac43eb1f0df53ccee7c49ae68bac48835017c8a4648398d8119c01dbd38af63826b6d5f4c49987ee6e630483e8c3a5784dbf84f5996e9ada58bca11902466230b9e9d3ae7b62bd8ad2682c6da1b289be58142027b63bfbb1fc8572e0fda5e4e5b1ef711fd51429aa0d39f56adb79db422818ff006834d0a3dc4ae184b228f949dc3cb5cbed51b54648eb9c75e6ba78c285f90e41efdc9f7aad0cd14c9b07229ff346fea4ff00e3deff00ef0fd6bd06ee7ca1668a404300ca720d2d48051451401fffd1fa72aacdfb9916e7f87eebff00ba7a1fc0fe99ab5514dfea9bbf1839f4ae6a90e68d8d62ecc8a0fdcbb5a9e8bf327fba7b7e078fcaad56647e634217acb6fcaffb4bd31f8f4fa8ad0475910488721864563869e9c8fa7f5f81751751f4d7708327927800752691dc2f1d49e83d6a055321dc4f07a91dc7a0f6f7eff4aea3239abc691ae6e4a1f9b29f779e36f3b4faff00f5f15971da4f291b7eef6c5747b55ef2ee3c7198b18ed85ed572de2109f980e7afbfb8fea3f1a6c08f4eb57857e6ad6650c30694003a52d48158168d8e7ea7dffda1fd47e3563af229194375edc8ed4a000001d0530168a28a407fffd2fa72a39466261ec6a4a42323158965139551728398d9b23d549e47f5a7a930b948c6e4906f43d867ae4f61de9f6df708fa7f2150ec6daf660e08f9e2f4c039c1fa1e3e95c959724d545fd7fc3ff91b45dd72b2644dff0033720f53fdeff01edf9d583d299148258c4838cf51e87b8fc2a4aea4d357464d5b4661db59dfa6a1733dc188c3291b36eedff2f0339e3a7a56d0518191d29d4550828a28a4014514500145145007ffd3fa728aaeb776ad234492a33a7de50c091db91f5a7f9f19e87f435916456fc332ff009ea47f4a9268d9c2bc780e872b9e9ee0fb115144479cd8e8738e3d307fad5ba99c54972b1a76774520724dc5bae1b3fbd8bbe7fc7dfa11f9d5a8e449503a1c8351cb131613427120e39e8c3d0ff9e2a104926e2dc1dc0e2588f527fc7d0f423f035c7194a8cacf6febfa6be68d5a52572ed14c8e449503c6720d3ebb134d5d19356d18514514c414514500145145007fffd4fa26df4a82dee1ee14b3162e403db7b6e233e99f4c7be6aef9117f7454d456572ca61163b81b4601fea3ff00ad572aacf80eaffe7a8fe99ab54802a09212cc2589b6480633d411e8477153d432498caa9c63a9eb8cff00327b0a994149598d36b54570a657675fdccca403dd5bfc7f98a9e39f2de54a3649e9d8fb83de9bf67491712af1d87a7e3ebea6a29018d365d0f3611ceffe24f738f4f51cff003ae594654758edfd7f57fbcd5352d197a8aadb6e235dd1b09940c807ef11f51c1f6e2a58e449537a1c8fd41f43ef5d10aaa5a7521c6da92514515a10145145007fffd5fa728a28ac4b2b5cfdc07ebffa09a9c9f973ed50dcfdcfcfff00413529fb9f8531951259252c09dbb7d28b53bce4f6191f524827ea7151db7593e94fb2e87fdd1fcda8e822fd1451480a90fee6ecdb27fab29bc0f439c71eded49723c89639a3e0c8e1187620f7fa8f5a07fc84bfed8ffecd4b7fff002c3febb2ff005af35e89dba3fd4e8fb5f2fd0b7451457a473851451401ffd9c266ae7f";
        BinTreeNodeReader reader = new BinTreeNodeReader();
		byte[] b = WhatsApi.hex2bin(locationMessage);
        ProtocolNode node = reader.nextTree(b);
        log.debug(node.toString());
        ProtocolNode mediaNode = node.getChild(0);
        assertNotNull(mediaNode);
        assertEquals("location", mediaNode.getAttribute("type"));
        assertEquals("24.6995", mediaNode.getAttribute("longitude"));
        assertEquals("60.1674", mediaNode.getAttribute("latitude"));
        assertEquals("raw", mediaNode.getAttribute("encoding"));
	}

}
