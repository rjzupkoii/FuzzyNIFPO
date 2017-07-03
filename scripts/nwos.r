#!/usr/local/bin/rscript

# Clean-up script for the NWOS data
MINIMUM_ACERAGE = 10

# Load the contents of the file and perform a basic data clean-up
loadData <- function(file) {
	raw <- read.csv(file, header = TRUE)
	raw = raw[, -c(1)]								# Toss out the row names								
	raw = raw[raw$Freq != 0, ]						# Toss out frequencies of zero
	raw = raw[raw$AC_WOOD >= MINIMUM_ACERAGE, ] 	# Toss out anything below our minimum acerage to include null responses
	
	# TODO Update everything to make use of tax data
	raw = raw[, -c(3)]
		
	# Toss out all of the invalid ACT_CUT_SALE responses before removing the column 
	raw = raw[raw$ACT_CUT_SALE != -1, ]
	raw = raw[, -c(2)]
		
	# Repalce parcel sizes with liguistic variables { small, medium, large }
	raw$parcel <- NA
	raw[raw$AC_WOOD < 40, ]$parcel = 'small'
	raw[raw$AC_WOOD >= 100, ]$parcel = 'large'
	raw[is.na(raw$parcel), ]$parcel = 'medium'
	
	# Drop the AC_WOOD column
	raw = raw[, -c(1)]							
	
	# Update the column order and return
	return(raw)
}

process <- function(file) {
	print(file)
			
	# Load the data and note the index of the relevent variable
	data = loadData(file)
	name = strsplit(basename(file), '\\.')[[1]][1]
	index = grep(name, colnames(data))
	
	# Process and aggregate the data
	data = data[data[index] > 0, ]				# Discard null responses
	data[data[index] == 8, ][index] = 1			# Rescore NA as no concern
	data = aggregate(Freq ~ ., data = data, sum)

	# Find the odds to use for the data
	data = findOdds(data)
	
	# Save the results
	file = paste('../nwos/clean/', basename(file), sep = '')
	write.csv(data, file, row.names = FALSE)
}

findOdds <- function(data) {
	for (label in c('small', 'medium', 'large')) {
		# Find the distributions for each group
		total = sum(data[data$parcel == label, ]$Freq)
		data[data$parcel == label, ]$Freq = data[data$parcel == label, ]$Freq / total
		
		# Make the distributions act as cummulative sums
		for (index in c(2:5)) {
			previous = data[data$parcel == label, ][index - 1, ]$Freq
			value = data[data$parcel == label, ][index, ]$Freq
			
			data[data$parcel == label, ][index, ]$Freq = previous + value
		}
	}
	return(data)
}

dir.create('../nwos/clean', showWarnings = FALSE)

process('../nwos/raw/CNC_CLIM.csv')
process('../nwos/raw/CNC_DEV.csv')
process('../nwos/raw/CNC_FIRE.csv')
process('../nwos/raw/CNC_TAX.csv')
process('../nwos/raw/CNC_TRES.csv')
#process('../nwos/raw/CUT_LOG_SALE.csv')

process('../nwos/raw/OBJ_BEA.csv')
process('../nwos/raw/OBJ_CHILD.csv')
process('../nwos/raw/OBJ_HUNT.csv')
process('../nwos/raw/OBJ_INV.csv')
process('../nwos/raw/OBJ_NAT.csv')
process('../nwos/raw/OBJ_PRI.csv')
process('../nwos/raw/OBJ_REC.csv')
process('../nwos/raw/OBJ_TIM.csv')
process('../nwos/raw/OBJ_WAT.csv')
process('../nwos/raw/OBJ_WIL.csv')
