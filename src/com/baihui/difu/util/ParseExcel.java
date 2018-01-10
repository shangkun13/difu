package com.baihui.difu.util;
/**
 * 解析excel文件
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.log4j.*;


public class ParseExcel {
	
	public static Logger log=Logger.getLogger(ParseExcel.class);

	/**
	 * 读取excel中所有sheet页中的数据,将其转换成List数组
	 * @param in
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<List<String>> readExcel(int type,InputStream in,int colomuNum) throws FileNotFoundException, IOException {
		log.info("ParseExcel/readExcel() method beginning!");
		// File file = new File("E:/新建文件夹/新建 WPS表格 工作簿.xls");
		//创建一个Excel文件  
		HSSFWorkbook wb = new HSSFWorkbook(in);
		//记录sheet页中所有记录的行数,每一行有都是一个数据,里面记录了单元格的信息
		List<List<String>> list = new ArrayList<List<String>>();
		log.info("一共有几个sheet页:["+wb.getNumberOfSheets()+"]");
		Util.message_normal+="<br />一共有"+wb.getNumberOfSheets()+"个sheet页";
		//读取sheet页
		for (int numSheets = 0; numSheets < wb.getNumberOfSheets(); numSheets++) {
			//sheet页存在
			if (wb.getSheetAt(numSheets) != null) {
				//读取其中一个Excel的Sheet页中的内容
				HSSFSheet aSheet = wb.getSheetAt(numSheets);
				//sheet页中的内容为空,第一行为title,
				/**
				 * 如果只有一行也不会进行导入,需要测试
				 */
				if (aSheet.getLastRowNum() < 1) {
					return list;
				}
				Util.message_normal+="<br />第"+(numSheets+1)+"个sheet页中有"+aSheet.getLastRowNum()+"行记录";
				System.out.println("第"+(numSheets+1)+"个sheet页中有"+aSheet.getLastRowNum()+"行记录");
				log.info("第"+(numSheets+1)+"个sheet页中有"+aSheet.getLastRowNum()+"行记录");
				//获取标题列,读取其中第三列.确定到底销售订单还是采购订单,如果第三列是购货单位-->销售单,如果第三列是收货仓库-->采购单
				HSSFCell titleCell=aSheet.getRow(0).getCell(2);
				log.info("excel中第一行第三列的值:"+"titleCell.getStringCellValue()");
				if("收货仓库".equals(Util.StrTrim(titleCell.getStringCellValue()))&&type!=0){//采购单
					log.info("您上传文件的类型是采购单,与您选择的上传文件的类型不一致!");
					Util.tip_error="您上传文件的类型是采购单,与您选择的上传文件的类型不一致!";
					
				}else if("购货单位".equals(Util.StrTrim(titleCell.getStringCellValue()))&&type!=1){//销售单
					Util.tip_error="您上传文件的类型是销售单,与您选择的上传文件的类型不一致!";
					log.info("您上传文件的类型是销售单,与您选择的上传文件的类型不一致!");
					
				}
				
				
				
				//遍历sheet页中的记录,rowNumOfSheet = 1,从第二行开始读,不读取第一行,因为第一行是title.
				for (int rowNumOfSheet = 1; rowNumOfSheet <= aSheet.getLastRowNum(); rowNumOfSheet++) {
					System.out.println("第"+rowNumOfSheet+"条数据");
					log.info("第["+rowNumOfSheet+"]条数据");
					//遍历的sheet页中的某一条记录不为空
					System.out.println("测试从0开始还是从1开始:"+aSheet.getRow(0));
					if (aSheet.getRow(rowNumOfSheet) != null) {
						//读取那条记录放到新建的aRow行里.HSSFRow创建新建行
						HSSFRow aRow = aSheet.getRow(rowNumOfSheet);
						//list2用来存储一行数据里的数据值,数据值的格式是string类型
						List<String> list2 = new ArrayList<String>();
						System.out.println("第"+(numSheets+1)+"sheet页中有"+aRow.getLastCellNum()+"列数据");
						log.info("第["+(numSheets+1)+"]sheet页中有["+aRow.getLastCellNum()+"]列数据");
						//遍历这一行里所有单元格的内容
						for (int cellNumOfRow = 0; cellNumOfRow < aRow.getLastCellNum(); cellNumOfRow++) {
							String strCell = "";
							//某一行中的某一列---->单元格
							if (aRow.getCell(cellNumOfRow) != null) {
								//获取了行所对应的的列的信息
								HSSFCell aCell = aRow.getCell(cellNumOfRow);
								//获取这一列所对应的数据类型
								int cellType = aCell.getCellType();
								System.out.println("第"+(cellNumOfRow+1)+"列:"+aCell);
								log.info("第["+(cellNumOfRow+1)+"]列:["+strCell+"]");
								//返回字符串的表示形式
								switch (cellType) {
									case 0:// number
										 if (HSSFDateUtil.isCellDateFormatted(aCell)) {// 处理日期格式、时间格式  
								             System.out.println("1");  
											 SimpleDateFormat sdf = null;  
								                if (aCell.getCellStyle().getDataFormat() == HSSFDataFormat  
								                        .getBuiltinFormat("h:mm")) {  
								                    sdf = new SimpleDateFormat("HH:mm");  
								                } else {// 日期  
								                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
								                }  
								                Date date = aCell.getDateCellValue();  
								                strCell = sdf.format(date);  
								            } else if (aCell.getCellStyle().getDataFormat() == 58) {  
								            	   System.out.println("2");  
								            	// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
								                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
								                double value = aCell.getNumericCellValue();  
								                Date date = org.apache.poi.ss.usermodel.DateUtil  
								                        .getJavaDate(value);  
								                strCell = sdf.format(date);  
								            } else {  
								            	   System.out.println("3");  
								            	BigDecimal bd = new BigDecimal(
												aCell.getNumericCellValue());
								            	strCell = bd.toPlainString();
								            }
										break;
									case 1:// string
										strCell = aCell.getStringCellValue();
										break;
									default:
//										System.out.println("默认数据");
//										 aCell.getRichStringCellValue();
								}
							}
							list2.add(strCell.trim());
						}
						/**
						 * 这句话的意思是如果读取的某一行数据中后面的字段为空,则将其置为null,保存到list2中,也让它保持有12条数据
						 */
						while (list2.size() < colomuNum) {
							list2.add(null);
						}
						//将一行数据添加到list集合中
						list.add(list2);
					}
				}
			}
		}
		Util.message_normal+="<br />一共有"+list.size()+"条记录";
		log.info("ParseExcel/readExcel() method end!");
		return list;
	}
	
}
