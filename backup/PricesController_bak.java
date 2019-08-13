package org.finance_price_service.entrypoint;

/*
@RestController
public class PricesController_bak {

  @RequestMapping("/query")
  public ResponsePrices response(@RequestParam(value = "symbol") String symbol,
      @RequestParam(value = "days", defaultValue = "0") int days) throws Exception {

    //QueryLogicService logic = new QueryLogicService();
    //return logic.query(symbol, days);
    final String myURL = "jdbc:mysql://localhost:3306/test";
    final String tableName = "prices";
    final String userName = "root";
    final String pwd = "alssss917";
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection conn = DriverManager.getConnection(myURL, userName, pwd);
    Statement st = conn.createStatement();

    ResponsePrices res = new ResponsePrices(symbol);
    for (int i = 0; i < days; ++i) {
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DATE, -i);
      String date = dateFormat.format(cal.getTime());
      final String selectQuery = String.format("SELECT * from %s WHERE ticker = '%s' AND date = '%s'", tableName, symbol, date);
      ResultSet rs = st.executeQuery(selectQuery);
      if (rs.next()) {
          OneDayPrice price = new OneDayPrice(date, rs.getDouble("open"), rs.getDouble("high"),
                                              rs.getDouble("low"), rs.getDouble("close"),
                                              rs.getInt("volume"));
          res.addPrice(price);
      }
    }
    conn.close();
    return res;
  }


  @RequestMapping("/update")
  public String update(@RequestParam(value = "symbol") String symbol, @RequestParam(value = "date", defaultValue = "NAN") String upDate) throws Exception {
    final String myURL = "jdbc:mysql://localhost/test";
    final String tableName = "prices";
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection conn = DriverManager.getConnection(myURL, "root", "alssss917");
    Statement st = conn.createStatement();
    final String apiKey = "RFISYPX8K6DYSF40";
    String urlString = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s", symbol, apiKey);
    try {
      JSONObject json = new JSONObject(IOUtils.toString(new URL(urlString), Charset.forName("UTF-8")));
      JSONObject array = json.getJSONObject("Time Series (Daily)");
      Iterator<String> dates = array.keys();
      while (dates.hasNext()) {
        String date = dates.next();
        JSONObject price = array.getJSONObject(date);
        final double open = Double.parseDouble(price.get("1. open").toString());
        final double high = Double.parseDouble(price.get("2. high").toString());
        final double low = Double.parseDouble(price.get("3. low").toString());
        final double close = Double.parseDouble(price.get("4. close").toString());
        final int volume = Integer.parseInt(price.get("5. volume").toString());
        final String insertQuery = String.format("INSERT INTO %s(ticker, date, open, high, low, close, volume) VALUES('%s', '%s', %f, %f, %f, %f, %d)"
            , tableName, symbol, date, open, high, low, close, volume);
        st.executeUpdate(insertQuery);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    conn.close();
    return String.format("Updated %s data successfully!", symbol);
  }
}
*/
  /**@Note Implementation without database*/
/*
@RestController
  public class PricesController {
    @RequestMapping("/query")
    public Map<String, Object> response(@RequestParam(value = "symbol", defaultValue = "NAN") String symbol,
        @RequestParam(value = "days", defaultValue = "0") int days)
    {
      final String apiKey = "RFISYPX8K6DYSF40";
      String urlString = String.format("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&outputsize=full&apikey=%s", symbol, apiKey);
      TreeMap<String, Object> jMap = new TreeMap<String, Object>();
      ResponsePrices resp = new ResponsePrices(symbol);
      try {
        JSONObject json = new JSONObject(IOUtils.toString(new URL(urlString), Charset.forName("UTF-8")));
        JSONObject array = json.getJSONObject("Time Series (Daily)");

        //Convert JSONObject to HashMap
        ObjectMapper mapper = new ObjectMapper();
        jMap = mapper.readValue(array.toString(), TreeMap.class);
      }
      catch(Exception e) {
        e.printStackTrace();
      }

      NavigableMap<String, Object> rjMap = jMap.descendingMap();
      TreeMap<String, Object> res = new TreeMap<String, Object>(Collections.reverseOrder());
      int it = 0;
      for (NavigableMap.Entry<String, Object> entry : rjMap.entrySet()) {
        res.put(entry.getKey(), entry.getValue());
        ++it;
        if (it >= days) break;
      }
      return res;
    }
  }
*/