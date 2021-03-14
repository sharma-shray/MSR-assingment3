public StringBuffer generate(StringBuffer tagBuffer) {
			tagBuffer.append("<!DOCTYPE ").append(name).append(" PUBLIC \"").append(systemUri).append("\">\n");
			//TODO A meaningful exception should be raised 
			// when trying to call the method for a HTMLDoctype 
			// with number of child != 1 
			return ((HTMLNode) children.get(0)).generate(tagBuffer);
		}