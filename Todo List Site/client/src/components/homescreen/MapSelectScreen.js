import React        from 'react';
import { WLayout, WButton, } from 'wt-frontend';
import WCard from 'wt-frontend/build/components/wcard/WCard';
import WLHeader from 'wt-frontend/build/components/wlayout/WLHeader';
import WLMain from 'wt-frontend/build/components/wmodal/WMMain';

const MapSelectScreen = () => {
    return(
        <WCard style={{height: "100%", width: "100vw" }} raised>
        <WLayout wLayout="header" className="example-layout-labels">
          <WLHeader className="Map-Select-Screen-Header">
              Your Maps
              </WLHeader>
          <WLMain style={{ backgroundColor: "ivory"}} className="Map-Select-Screen-Main">
                            <WCard style={{height: "100%", width: "53%" }} raised>
                            <WLayout wLayout="footer" style={{ backgroundColor: "gray"}} className="example-layout-labels">
                            </WLayout>
                            </WCard>
                    <WButton className="Create-New-Map" clickAnimation="ripple-light" hoverAnimation="darken" >
                    Create New Map
                    </WButton>
              </WLMain>
        </WLayout>
      </WCard>


    );
}

export default MapSelectScreen;