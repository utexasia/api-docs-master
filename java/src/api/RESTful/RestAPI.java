package api.RESTful;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import api.utils.Base64CoderC;
import api.utils.HttpUtil;

public class RestAPI {

	// 网站申请的密钥对
	private final static String accessKey = "";
	private final static String secretKey = "";

	private final static String URL = "http://api.utexasia.com";

	// 交易市场配置
	@Test
	public void getMarketConfig() {
		String text = HttpUtil.get(URL + "/data/api/v1/getMarketConfig");
		System.out.println("交易市场配置:"+text);
	}

	// K线数据
	@Test
	public void getKLine() {
		String text = HttpUtil.get(URL + "/data/api/v1/getKLine?market=btc_usdt&type=1min&since=0");
		System.out.println("K线数据:"+text);
	}

	// 聚合行情（Ticker）
	@Test
	public void getTicker() {
		String text = HttpUtil.get(URL + "/data/api/v1/getTicker?market=btc_usdt");
		System.out.println("聚合行情（Ticker）:"+text);
	}

	// 所有市场的最新 All Tickers
	@Test
	public void getTickers() {
		String text = HttpUtil.get(URL + "/data/api/v1/getTickers");
		System.out.println("所有市场的最新 All Tickers:"+text);
	}

	// 市场深度数据 Depth
	@Test
	public void getDepth() {
		String text = HttpUtil.get(URL + "/data/api/v1/getDepth?market=btc_usdt");
		System.out.println("市场深度数据 Depth:"+text);
	}

	// 最近市场成交记录 Trades
	@Test
	public void getTrades() {
		String text = HttpUtil.get(URL + "/data/api/v1/getTrades?market=btc_usdt");
		System.out.println("最近市场成交记录 Trades:"+text);
	}

	// 获取交易账户资产
	@Test
	public void getBalance() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accesskey", accessKey);
		map.put("nonce",  System.currentTimeMillis());
		// 签名
		String signature = HttpUtil.getSignature(map, secretKey);
		map.put("signature", signature);

		String text = HttpUtil.get(URL + "/trade/api/v1/getBalance", map);
		System.out.println(text);
	}

	// 获取账户类型
	@Test
	public void getAccounts() {
		String text = HttpUtil.get(URL + "/trade/api/v1/getAccounts");
		System.out.println(text);
	}

	// 获取指定账户资金
	@Test
	public void getFunds() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accesskey", accessKey);
		map.put("account", 2);	// 现货账户
		map.put("nonce", System.currentTimeMillis());
		// 签名
		String signature = HttpUtil.getSignature(map, secretKey);
		map.put("signature", signature);
		String text = HttpUtil.get(URL + "/trade/api/v1/getFunds", map);
		System.out.println(text);
	}

	// 划账
	@Test
	public void transfer() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accesskey", accessKey);
		map.put("from", 2);
		map.put("to", 3);
		map.put("amount", 0.1);
		map.put("coin", "btc");
		map.put("safePwd", "qwer1234");
		map.put("nonce", System.currentTimeMillis());
		// 签名
		String signature = HttpUtil.getSignature(map, secretKey);
		map.put("signature", signature);
		String text = HttpUtil.post(URL + "/trade/api/v1/transfer", map);
		System.out.println(text);
	}

	// 委托
	@Test
	public void order() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accesskey", accessKey);
		map.put("nonce", System.currentTimeMillis());
		map.put("market", "eth_usdt");
		map.put("price", "0.01");
		map.put("number", "0.001");
		map.put("type", 1);		// 0.sell 1.buy
		map.put("entrustType", 0);	// 0.Limited price  1.Market price matching
		// 签名
		String signature = HttpUtil.getSignature(map, secretKey);
		map.put("signature", signature);
		//
		String text = HttpUtil.post(URL + "/trade/api/v1/order", map);
		System.out.println(text);
	}

	// 批量委托
	@Test
	public void batchOrder() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accesskey", accessKey);
		map.put("nonce", System.currentTimeMillis());
		map.put("market", "btc_usdt");

		JSONArray array = new JSONArray();
		for(int i = 0; i < 10; i++) {
			JSONObject bid = new JSONObject();
			bid.put("price", "1.123");
			bid.put("amount", "0.1");
			bid.put("type", 1);
			array.add(bid);
			JSONObject ask = new JSONObject();
			ask.put("price", "11.123");
			ask.put("amount", "0.1");
			ask.put("type", 0);
			array.add(ask);
		}
		// put data
		String data = Base64CoderC.encode(array.toJSONString());

		map.put("data", data);

		// 签名
		String signature = HttpUtil.getSignature(map, secretKey);
		map.put("signature", signature);
		//
		String text = HttpUtil.post(URL + "/trade/api/v1/batchOrder", map);
		System.out.println(text);
	}

	// 撤单
	@Test
	public void cancel() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accesskey", accessKey);
		map.put("nonce",System.currentTimeMillis());
		map.put("market", "btc_usdt");
		map.put("id", "159171932500044");
		// 签名
		String signature = HttpUtil.getSignature(map, secretKey);
		map.put("signature", signature);

		String text = HttpUtil.post(URL + "/trade/api/v1/cancel", map);
		System.out.println(text);
	}

	// 批量撤单
	@Test
	public void batchCancel() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accesskey", accessKey);
		map.put("nonce", System.currentTimeMillis());
		map.put("market", "btc_usdt");

		JSONArray array = new JSONArray();
		array.add("159171946800063");
		array.add("159171946800064");
		array.add("159171946800065");
		array.add("159171946800066");
		array.add("159171946800067");

		// put data
		String data = Base64CoderC.encode(array.toJSONString());

		map.put("data", data);

		// 签名
		String signature = HttpUtil.getSignature(map, secretKey);
		map.put("signature", signature);
		//
		String text = HttpUtil.post(URL + "/trade/api/v1/batchCancel", map);
		System.out.println(text);
	}

	// 获取订单
	@Test
	public void getOrder() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accesskey", accessKey);
		map.put("nonce", System.currentTimeMillis());
		map.put("market", "btc_usdt");
		map.put("id", "159171946800082");
		// 签名
		String signature = HttpUtil.getSignature(map, secretKey);
		map.put("signature", signature);

		String text = HttpUtil.get(URL + "/trade/api/v1/getOrder", map);
		System.out.println(text);
	}

	// 获取未完成的订单
	@Test
	public void getOpenOrders() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accesskey", accessKey);
		map.put("nonce", System.currentTimeMillis());
		map.put("market", "btc_usdt");
		map.put("page", 1);
		map.put("pageSize", 10);

		// 签名
		String signature = HttpUtil.getSignature(map, secretKey);
		map.put("signature", signature);

		String text = HttpUtil.get(URL + "/trade/api/v1/getOpenOrders", map);
		System.out.println(text);
	}

	// 批量获取订单
	@Test
	public void getBatchOrders() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accesskey", accessKey);
		map.put("nonce",System.currentTimeMillis());
		map.put("market", "btc_usdt");

		JSONArray array = new JSONArray();
		array.add("159171946800079");
		array.add("159171946800078");
		array.add("159171946800077");
		array.add("159171946800076");
		array.add("159171946800075");

		// put data
		String data = Base64CoderC.encode(array.toJSONString());

		map.put("data", data);
		// 签名
		String signature = HttpUtil.getSignature(map, secretKey);
		map.put("signature", signature);

		String text = HttpUtil.get(URL + "/trade/api/v1/getBatchOrders", map);
		System.out.println(text);
	}

	// 获取充值记录
    @Test
    public void getPayInRecord() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("accesskey", accessKey);
        map.put("nonce", System.currentTimeMillis());
        map.put("coin", "eth");
        map.put("page", 1);
        map.put("pageSize", 10);

        // 签名
        String signature = HttpUtil.getSignature(map, secretKey);
        map.put("signature", signature);

        String text = HttpUtil.get(URL + "/trade/api/v1/getPayInRecord", map);
        System.out.println(text);
    }

    // 获取提现记录
    @Test
    public void getPayOutRecord() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("accesskey", accessKey);
        map.put("nonce", System.currentTimeMillis());
        map.put("coin", "eth");
        map.put("page", 1);
        map.put("pageSize", 10);
        // 签名
        String signature = HttpUtil.getSignature(map, secretKey);
        map.put("signature", signature);

        String text = HttpUtil.get(URL + "/trade/api/v1/getPayOutRecord", map);
        System.out.println(text);
    }

    // 获取充值地址
	@Test
	public void getPayInAddress() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accesskey", accessKey);
		map.put("nonce", System.currentTimeMillis());
		map.put("coin", "eth");
		// 签名
		String signature = HttpUtil.getSignature(map, secretKey);
		map.put("signature", signature);

		String text = HttpUtil.get(URL + "/trade/api/v1/getPayInAddress", map);
		System.out.println(text);
	}

	// 获取提现地址
	@Test
	public void getPayOutAddress() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accesskey", accessKey);
		map.put("nonce", System.currentTimeMillis());
		map.put("coin", "eth");
		// 签名
		String signature = HttpUtil.getSignature(map, secretKey);
		map.put("signature", signature);
		String text = HttpUtil.get(URL + "/trade/api/v1/getPayOutAddress", map);
		System.out.println(text);
	}

	// 提现
	@Test
	public void withdraw() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accesskey", accessKey);
		map.put("nonce", System.currentTimeMillis());
		map.put("coin", "eth");
		map.put("safePwd", "");
		map.put("amount", "1");
		map.put("address", "");
		// 签名
		String signature = HttpUtil.getSignature(map, secretKey);
		map.put("signature", signature);
		String text = HttpUtil.get(URL + "/trade/api/v1/withdraw", map);
		System.out.println(text);
	}

	// To be continued...

}
