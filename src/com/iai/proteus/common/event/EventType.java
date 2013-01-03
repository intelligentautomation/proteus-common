package com.iai.proteus.common.event;

/**
 * Event Types
 *
 * @author Jakob Henriksson
 *
 */
public enum EventType {

	// used to notify that the discovery layer should be enabled
	DISCOVERY_LAYER_ENABLE,
	// used to notify that the discovery layer should be disabled
	DISCOVERY_LAYER_DISABLE,
	// used to notify that the discovery layer was enabled
	DISCOVERY_LAYER_ENABLED,
	// used to notify that the discovery layer was disabled
	DISCOVERY_LAYER_DISABLED,
	// used to notify that the region constrain should be cleared/disabled
	DISCOVERY_LAYER_REGION_DISABLE,
	// used to notify that the region was enabled
	DISCOVERY_LAYER_REGION_ENABLED,
	// used to notify that the region was cleared/disabled
	DISCOVERY_LAYER_REGION_DISABLED,
	// used to notify of an update in faceted search details
	DISCOVERY_FACET_DATA_CHANGE,
	// used to notify of an update/change in a faceted search specification
	DISCOVERY_FACET_CHANGE,
	// used to notify that all facet constraints should be cleared
	DISCOVERY_FACET_CLEAR,

	// used to notify the data table viewer that the data has been updated
	DATA_TABLE_VIEWER_UPDATE,
	// used to ask for data to display
	DATA_TABLE_VIEWER_DATA_REQUEST,
	// used to notify the data table viewer that the table should be cleared
	DATA_TABLE_VIEWER_CLEAR,

	// used to ask for data to display
	FACET_VIEW_DATA_REQUEST,

	// used to toggle layers
	MAP_TOGGLE_LAYER,
	// used to toggle between globe/flat world
	MAP_TOGGLE_GLOBE_TYPE, 

}
