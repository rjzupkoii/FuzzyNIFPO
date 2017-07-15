#!/usr/local/bin/rscript

require(ggplot2)
require(reshape2)

experiments = c('fis')
outDirectory = '../out/plots/'
timeSteps = 199

colSd <- function (x, na.rm = F) apply(X = x, MARGIN = 2, FUN = sd, na.rm = na.rm)

# http://stackoverflow.com/a/24241954/1185
fancy_scientific <- function(value) {
     value <- format(value, scientific = TRUE)
     value <- gsub("^(.*)e", "'\\1'e", value)
     value <- gsub("e", "%*%10^", value)
     parse(text = value)
}

analysis <- function (file, title, ylabel, fancy) {
	
	# Read the relevent data from disk
	data <- loadData(file)
	rows = nrow(data[['fis']])
	
	# Prepare the data farme with the mean of the data
	df <- data.frame(Year = 1:timeSteps,
					 'fis' = colMeans(data[['fis']]))
		
	df <- melt(df, id.vars = 'Year', variable.name = 'Series')
	df$Series <- as.character(df$Series)
	df$Series[df$Series == "fis"] <- "FIS"
	
	# Plot and save
	plot(df, rows, title, ylabel, fancy, file)
}

harvestAnalysis <- function(title, ylabel, fancy) {
	# Read the relevent data from disk
	stems <- loadData('harvestedStems')
	biomass <- loadData('harvestedBiomass')
	rows = nrow(stems[['fis']])
		
	# Prepare the data frame wtih the mean of the data
	df <- data.frame(Year = 1:timeSteps,
			'none.biomass' = colMeans(biomass[['fis']]),
			'none.stems' = colMeans(stems[['fis']]))
	
	# Melt and set the labels
	df <- melt(df, id.vars = 'Year', variable.name = 'Series')
	df$Series <- as.character(df$Series)
	df$Series[df$Series == "fis.biomass"] <- "Biomass"
	df$Series[df$Series == "fis.stems"] <- "Stems"

	# Plot and save
	plot(df, rows, title, ylabel, fancy, 'harvestedBiomass')
}

loadData <- function(file) {
	data <- list()
	for (experiment in experiments) {
		path = paste('../out/', experiment, '/', file, '.csv', sep="")
		working <- read.csv(path, header=F)
		data[[experiment]] <- working[, 0:timeSteps]
	}
	return(data)
}

plot <- function(df, rows, title, ylabel, fancy, file) {
	title = sprintf("%s (mean of %i runs)", title, rows)
	plotted <- ggplot(df, aes(Year, value), main = title) +
			geom_line(aes(colour = Series)) +
			labs(y = ylabel, title = title) +
			theme(legend.position = "none", legend.title = element_blank())
	
	if (fancy) {
		plotted <- plotted + scale_y_continuous(labels = fancy_scientific)
	}
	
	file = paste(outDirectory, file, '.png', sep="")
	ggsave(filename = file, plot = plotted)	
}

main <- function() {
	dir.create(outDirectory, showWarnings = FALSE)

	analysis('carbonAgents', 'Carbon Sequestration by NIPFOs', expression('Metric Tons (MTCO'[2]*')'), F)
	analysis('carbonGlobal', 'Global Carbon Sequestration', expression('Metric Tons (MTCO'[2]*')'), F)
	
	harvestAnalysis('Harvested Biomass & Stems', 'Metric Tons (MT) Dry Weight', T)
	analysis('harvestDemand', 'Harvest Demand', 'Owners', F)
	analysis('harvestedParcels', 'Harvested Parcels', 'Owners', F)
	
	#analysis('harvestedBiomass', 'Harvested Biomass', 'Metric Tons (MT) Dry Weight', T)
	#analysis('harvestedStems', 'Harvested Stems', 'Metric Tons (MT) Dry Weight', T)
}
main()
