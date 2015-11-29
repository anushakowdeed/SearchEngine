public String getPostings(long offset, String fileName){
		try {
			String result;
			RandomAccessFile reader = new RandomAccessFile(fileName, "r");
			BufferedReader r1 = new BufferedReader(new FileReader(reader.getFD()));
			if(offset >= 0){
				reader.seek(offset);
				result = r1.readLine();
				reader.close();
				r1.close();
				return result;
			}
			reader.close();
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}



