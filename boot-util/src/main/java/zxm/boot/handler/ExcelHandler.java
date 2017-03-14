package zxm.boot.handler;

import zxm.boot.exc.HandlerException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 封装对excel的操作，包括本地读写excel和流中输出excel,支持office 2007。<br/>
 * 依赖于poi-3.9-20121203.jar,poi-ooxml-3.9-20121203.jar,poi-ooxml-schemas-3.9-
 * 20121203.jar,dom4j-1.6.1.jar<br/>
 * 有参构造函数参数为excel的全路径<br/>
 * 
 * @author alex.hu
 * @date May 26, 2016
 */
public class ExcelHandler {
	static Logger logger = LoggerFactory.getLogger(ExcelHandler.class);
	
	// 写入excel时，是否自动扩展列宽度来符合内容。
	private static boolean autoColumnWidth = true;

	/**
	 * 读取某个工作簿上的所有单元格的值。
	 * 
	 * @param sheetOrder 工作簿序号，从0开始。
	 * @return List<Object[]> 所有单元格的值。
	 * @throws IOException 加载excel文件IO异常。
	 * @throws FileNotFoundException excel文件没有找到异常。
	 * @throws InvalidFormatException
	 */
	public static List<Object[]> read(int sheetOrder, String path){
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			
			return read(fis, sheetOrder, 1, 1);
		} catch (IOException e) {
			logger.error("读取excel文件失败！", e);
			throw new HandlerException("读取excel文件失败！", e);
		}finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @param is 输入流
	 * @param sheetOrder 工作簿序号,从0开始。
	 * @param rowBegin 行数 从0开始
	 * @param colBegin 列数 从1开始
	 * @return
	 */
	public static List<Object[]> read(InputStream is, int sheetOrder, int rowBegin, int colBegin) {
		// 用来记录excel值
		List<Object[]> valueList = new LinkedList<Object[]>();
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(is);
			Sheet sheet = workbook.getSheetAt(sheetOrder);

			int size = sheet.getLastRowNum();
			if (size < rowBegin){
				return new ArrayList<>();
			}

			for (int i = 0; i < size - rowBegin + 1; i++) {
				Row row = sheet.getRow(rowBegin + i);
				// 每一行
				Object[] rowObject = null;
				for (Cell cell : row) {
					// cell.getCellType是获得cell里面保存的值的type
					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_BOOLEAN:
							// 得到Boolean对象的方法
							rowObject = ArrayUtils.add(rowObject, cell.getBooleanCellValue());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							// 先看是否是日期格式
							if (DateUtil.isCellDateFormatted(cell)) {
								// 读取日期格式
								rowObject = ArrayUtils.add(rowObject, cell.getDateCellValue());
							} else {
								DecimalFormat df = new DecimalFormat();
								// 单元格的值,替换掉,
								String value = df.format(cell.getNumericCellValue()).replace(",", "");
								// 读取数字
								rowObject = ArrayUtils.add(rowObject, value);
							}
							break;
						case Cell.CELL_TYPE_FORMULA:
							// 读取公式
							rowObject = ArrayUtils.add(rowObject, cell.getCellFormula());
							break;
						case Cell.CELL_TYPE_STRING:
							// 读取String
							rowObject = ArrayUtils.add(rowObject, cell.getRichStringCellValue().toString());
							break;
					}
				}
				// 将这行添加到list。
				valueList.add(rowObject);
			}

		} catch (InvalidFormatException | IOException e) {
			logger.error("读取excel文件失败！", e);
			throw new HandlerException("读取excel文件数据失败！", e);
		}
		
		return valueList;
	}

	/**
	 * 读取某个工作簿上的某个单元格的值。
	 * 
	 * @param sheetOrder 工作簿序号,从0开始。
	 * @param colum 列数 从1开始
	 * @param row 行数 从1开始
	 * @return 单元格的值。
	 * @throws Exception 加载excel异常。
	 */
	public static String read(int sheetOrder, int colum, int row, String path) throws Exception {
		FileInputStream fis = new FileInputStream(path);
		Workbook workbook = WorkbookFactory.create(fis);
		if (fis != null) {
			fis.close();
		}
		Sheet sheet = workbook.getSheetAt(sheetOrder);
		Row rows = sheet.getRow(row - 1);
		Cell cell = rows.getCell(colum - 1);
		String content = cell.getStringCellValue();
		return content;
	}

	/**
	 * 在指定的工作簿、行、列书写值。
	 * 
	 * @param sheetOrder 工作簿序号，基于0.
	 * @param colum 列 基于1
	 * @param row 行 基于1
	 * @param content 将要被书写的内容。
	 * @throws Exception 书写后保存异常。
	 */
	public static void write(int sheetOrder, int colum, int row, String content, String path) throws Exception {
		FileInputStream fis = new FileInputStream(path);
		Workbook workbook = WorkbookFactory.create(fis);
		if (fis != null) {
			fis.close();
		}
		Sheet sheet = workbook.getSheetAt(sheetOrder);
		Row rows = sheet.createRow(row - 1);
		Cell cell = rows.createCell(colum - 1);
		cell.setCellValue(content);
		FileOutputStream fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
		fileOut.close();

	}

	/**
	 * 得到一个工作区最后一条记录的序号，相当于这个工作簿共多少行数据。
	 * 
	 * @param sheetOrder 工作区序号
	 * @return int 序号。
	 * @throws IOException
	 *             根据excel路径加载excel异常。
	 * @throws InvalidFormatException
	 */
	public static int getSheetLastRowNum(int sheetOrder, String path){
		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			if (fis != null) {
				fis.close();
			}
			return getSheetLastRowNum(sheetOrder, fis);
		} catch (IOException e) {
			logger.error("读取excel文件失败！", e);
			throw new HandlerException("读取excel文件失败！", e);
		}
	}
	
	public static int getSheetLastRowNum(int sheetOrder, InputStream is){
		try {
			Workbook workbook = WorkbookFactory.create(is);
			Sheet sheet = workbook.getSheetAt(sheetOrder);
			return sheet.getLastRowNum();
		} catch (InvalidFormatException | IOException e) {
			logger.error("读取excel文件数据失败！", e);
			throw new HandlerException("读取excel文件数据失败！", e);
		}
	}

	/**
	 * 在磁盘生成一个含有内容的excel,路径为path属性
	 * 
	 * @param sheetName 导出的sheet名称
	 * @param fieldName 列名数组
	 * @param data 数据组
	 * @throws IOException
	 */
	public static void makeExcel(String sheetName, String[] fieldName, List<Object[]> data, String path) throws IOException {
		// 在内存中生成工作薄
		HSSFWorkbook workbook = makeWorkBook(sheetName, fieldName, data);
		// 截取文件夹路径
		String filePath = path.substring(0, path.lastIndexOf("\\"));
		// 如果路径不存在，创建路径
		File file = new File(filePath);
		// System.out.println(path+"-----------"+file.exists());
		if (!file.exists()){
			file.mkdirs();
		}
		FileOutputStream fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
		fileOut.close();
	}

	/**
	 * 在输出流中导出excel。
	 * 
	 * @param excelName
	 *            导出的excel名称 包括扩展名
	 * @param sheetName
	 *            导出的sheet名称
	 * @param fieldName
	 *            列名数组
	 * @param data
	 *            数据组
	 * @param response
	 *            response
	 * @throws IOException
	 *             转换流时IO错误
	 */
	public static void makeStreamExcel(String excelName, String sheetName, String[] fieldName, List<Object[]> data,
			HttpServletResponse response) throws IOException {
		OutputStream os = null;
		response.reset(); // 清空输出流
		os = response.getOutputStream(); // 取得输出流
		response.setHeader("Content-disposition",
				"attachment; filename=" + new String(excelName.getBytes(), "ISO-8859-1")); // 设定输出文件头
		response.setContentType("application/msexcel"); // 定义输出类型
		// 在内存中生成工作薄
		HSSFWorkbook workbook = makeWorkBook(sheetName, fieldName, data);
		os.flush();
		workbook.write(os);
	}

	/**
	 * 根据条件，生成工作薄对象到内存。
	 * 
	 * @param sheetName
	 *            工作表对象名称
	 * @param fieldName
	 *            首列列名称
	 * @param data
	 *            数据
	 * @return HSSFWorkbook
	 */
	@SuppressWarnings("all")
	private static HSSFWorkbook makeWorkBook(String sheetName, String[] fieldName, List<Object[]> data) {
		// 用来记录最大列宽,自动调整列宽。
		Integer collength[] = new Integer[fieldName.length];

		// 产生工作薄对象
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 产生工作表对象
		HSSFSheet sheet = workbook.createSheet();
		// 为了工作表能支持中文,设置字符集为UTF_16
		workbook.setSheetName(0, sheetName);
		// 产生一行
		HSSFRow row = sheet.createRow(0);
		// 产生单元格
		HSSFCell cell;
		// 写入各个字段的名称
		for (int i = 0; i < fieldName.length; i++) {
			// 创建第一行各个字段名称的单元格
			cell = row.createCell((short) i);
			// 设置单元格内容为字符串型
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			// 为了能在单元格中输入中文,设置字符集为UTF_16
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			// 给单元格内容赋值
			cell.setCellValue(new HSSFRichTextString(fieldName[i]));
			// 初始化列宽
			collength[i] = fieldName[i].getBytes().length;
		}
		// 临时单元格内容
		String tempCellContent = "";
		// 写入各条记录,每条记录对应excel表中的一行
		for (int i = 0; i < data.size(); i++) {
			Object[] tmp = data.get(i);
			// 生成一行
			row = sheet.createRow(i + 1);
			for (int j = 0; j < tmp.length; j++) {
				cell = row.createCell((short) j);
				// 设置单元格字符类型为String
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				tempCellContent = (tmp[j] == null) ? "" : tmp[j].toString();
				cell.setCellValue(new HSSFRichTextString(tempCellContent));

				// 如果自动调整列宽度。
				if (autoColumnWidth) {
					if (j >= collength.length) { // 标题列数小于数据列数时。
						collength = ArrayUtils.add(collength, tempCellContent.getBytes().length);
					} else {
						// 如果这个内容的宽度大于之前最大的，就按照这个设置宽度。
						if (collength[j] < tempCellContent.getBytes().length) {
							collength[j] = tempCellContent.getBytes().length;
						}
					}
				}
			}
		}

		// 自动调整列宽度。
		if (autoColumnWidth) {
			// 调整列为这列文字对应的最大宽度。
			for (int i = 0; i < fieldName.length; i++) {
				sheet.setColumnWidth(i, collength[i] * 2 * 256);
			}
		}
		return workbook;
	}
	
	
	public static void main(String[] args) {
		List<Object[]> list = read(0, "C:/Users/Alex Hu/Desktop/中国行政区划分2016.8.31.xlsx");
		
		Map<String, Map<String, String>> provinceMap = new HashMap<>();
		Map<String, Map<String, Map<String, String>>> cityMap = new HashMap<>();
		Map<String, Map<String, Map<String, String>>> zoneMap = new HashMap<>();
		
		
		for (int i = 0; i < list.size(); i++) {
			Object[] objects = list.get(i);
			
			if(objects == null){
				continue;
			}
			
			String proviceStr = objects[3].toString();
			Map<String, String> pMap = provinceMap.get(proviceStr);
			if(pMap == null){
				pMap = new HashMap<>();
				pMap.put("proviceName", proviceStr);
				//pMap.put("provinceCode", objects[9].toString());
			}
			provinceMap.put(proviceStr, pMap);
			
			Map<String, Map<String, String>> cMap = cityMap.get(proviceStr);
			if(cMap == null){
				cMap = new HashMap<>();
			}
			
			String cityStr = objects[4].toString();
			Map<String, String> cInfoMap = cMap.get(cityStr);
			if(cInfoMap == null){
				cInfoMap = new HashMap<>();
			}
			
			cInfoMap.put("cityName", cityStr);
			//cInfoMap.put("cityCode", objects[10].toString());
			
			cMap.put(cityStr, cInfoMap);
			cityMap.put(proviceStr, cMap);
				
			Map<String, Map<String, String>> zMap = zoneMap.get(cityStr);
			if(zMap == null){
				zMap = new HashMap<>();
			}
			
			String zoneStr = objects[5].toString();
			Map<String, String> zInfoMap = zMap.get(zoneStr);
			if(zInfoMap == null){
				zInfoMap = new HashMap<>();
			}
			
			zInfoMap.put("zoneName", zoneStr);
			//zInfoMap.put("zoneCode", objects[11].toString());
			zInfoMap.put("zipcode", objects[1].toString());
			zInfoMap.put("telCode", "0" + objects[2]);
			
			zMap.put(zoneStr, zInfoMap);
			zoneMap.put(cityStr, zMap);
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO 'area' ('id','parent_id','tree_path','area_code','area_name','area_pinyin','bank_code','zipcode','tel_code','create_time','update_time') VALUES ");
		buffer.append("\r");
		buffer.append(" (1,0,'0','01000000','中国','"+StringHandler.getPinyinString("中国", null)+"','','','',SYSDATE(),NULL),");
		buffer.append("\r");
		int index = 1;
		int total = index;
		
		int i = 0;
		for (Map<String, String> province : provinceMap.values()) {
			i += 1;
			index += 1;
			int proviceIndex = index;
			
			buffer.append(" ("+index+","+total+",'"+total + "/"+"','01"+
			StringUtils.leftPad(String.valueOf(i), 2, "0")+"0000','"+province.get("proviceName")+"','"+
					StringHandler.getPinyinString(province.get("proviceName"), null)+"','','','',SYSDATE(),NULL),");
			buffer.append("\r");
			Map<String, Map<String, String>> cMap = cityMap.get(province.get("proviceName"));
			
			int j = 0;
			for (Map<String, String> city : cMap.values()) {
				j += 1;
				index += 1;
				int cityIndex = index;
				
				buffer.append(" ("+index+","+proviceIndex+",'"+total + "/"+proviceIndex + "/"+"','01"+
						StringUtils.leftPad(String.valueOf(i), 2, "0")+""+
						StringUtils.leftPad(String.valueOf(j), 2, "0")+"00','"+city.get("cityName")+"','"+
						StringHandler.getPinyinString(city.get("cityName"), null)+"','','','',SYSDATE(),NULL),");
				buffer.append("\r");
				Map<String, Map<String, String>> zMap = zoneMap.get(city.get("cityName"));
				
				int k = 0;
				for (Map<String, String> zone : zMap.values()) {
					k += 1;
					index += 1;
					
					buffer.append(" ("+index+","+cityIndex+",'"+total + "/"+proviceIndex + "/"+cityIndex + "/"+"','01"+
							StringUtils.leftPad(String.valueOf(i), 2, "0")+
							StringUtils.leftPad(String.valueOf(j), 2, "0")+
							StringUtils.leftPad(String.valueOf(k), 2, "0")+"','"+zone.get("zoneName")+"','"+
							StringHandler.getPinyinString(zone.get("zoneName"), null)+"','','"+
							zone.get("zipcode")+"','"+zone.get("telCode")+"',SYSDATE(),NULL),");
					buffer.append("\r");
				}
				
			}
		}
		
		System.out.println(buffer.toString());
		
		/*List<Object[]> list = read(1, "C:/Users/Alex Hu/Desktop/科室及就诊原因2016.8.23.xlsx");
		
		Map<String, Set<String>> map = new HashMap<>();
		
		for (Object[] objects : list) {
			Object obj = objects[0];
			if(obj == null){
				continue;
			}
			String objStr = obj.toString();
			if(objStr.indexOf("编号") != -1 || objStr.indexOf("汇总") != -1 || objStr.indexOf("总计") != -1){
				continue;
			}
			
			Object speObj = objects[1];
			if(speObj == null){
				continue;
			}
			
			String spe = speObj.toString();
			Set<String> set = map.get(spe);
			if(set == null){
				set = new HashSet<>();
			}
			
			set.add(objects[2].toString());
			map.put(spe, set);
		}
		
		StringBuffer speBuffer = new StringBuffer();
		speBuffer.append("INSERT INTO `datadict` (`id`,`data_type`,`parent_id`,`content`,`data_code`,`create_time`,`update_time`) VALUES ");
		speBuffer.append("\r");
		int speId = 0;
		
		StringBuffer reaBuffer = new StringBuffer();
		reaBuffer.append("INSERT INTO `datadict` (`id`,`data_type`,`parent_id`,`content`,`data_code`,`create_time`,`update_time`) VALUES ");
		reaBuffer.append("\r");
		int reaId = map.size();
		
		Set<Entry<String, Set<String>>> setList = map.entrySet();
		for (Entry<String, Set<String>> entry : setList) {
			speId++;
			
			speBuffer.append("("+ speId +",2,NULL,'"+entry.getKey()+"','',SYSDATE(),NULL),");
			speBuffer.append("\r");
			Set<String> sets = entry.getValue();
			for (String string : sets) {
				reaId++;
				reaBuffer.append("("+reaId+", 1, "+speId+",'"+string+"','',SYSDATE(),NULL),");
				reaBuffer.append("\r");
			}
		}
		
		System.out.println(speBuffer.toString());
		System.out.println(reaBuffer.toString());*/
		
		/*try {
			InputStream is = new FileInputStream(new File("C:/Users/Alex Hu/Desktop/1.xlsm"));
			List<Object[]> objList = ExcelHandler.read(is, 0, 1, 1);
			
			for (Object[] obj : objList) {
				String checkType = StringHandler.filterNull(obj[0]);
				String serviceCode = StringHandler.filterNull(obj[1]);
				String serviceName = StringHandler.filterNull(obj[2]);
				String nameEn = StringHandler.filterNull(obj[3]);
				String deviceCode = StringHandler.filterNull(obj[4]);
				String targetName = StringHandler.filterNull(obj[5]);
				String targetProject = StringHandler.filterNull(obj[6]);
				String targetPosition = StringHandler.filterNull(obj[7]);
				String checkWay = StringHandler.filterNull(obj[8]);
				double fee = Double.valueOf(StringHandler.filterNull(obj[9]));
				String desc = StringHandler.filterNull(obj[10]);

				System.out.println(checkType + " " + serviceCode + " " + serviceName  + " " + nameEn + " " + deviceCode
						 + " " + targetName + " " + targetProject + " " + targetPosition + " " + checkWay
						 + " " + fee + " " + desc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}