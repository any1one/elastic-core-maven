/*
 * Copyright © 2013-2016 The Nxt Core Developers.
 * Copyright © 2016-2017 Jelurida IP B.V.
 *
 * See the LICENSE.txt file at the top-level directory of this distribution
 * for licensing information.
 *
 * Unless otherwise agreed in a custom licensing agreement with Jelurida B.V.,
 * no part of the Nxt software, including this file, may be copied, modified,
 * propagated, or distributed except according to the terms contained in the
 * LICENSE.txt file.
 *
 * Removal or modification of this copyright notice is prohibited.
 *
 */

package org.xel.peer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import org.xel.Nxt;
import org.xel.util.Convert;

import java.util.List;

final class GetNextBlockIdsComputation extends PeerServlet.PeerRequestHandler {

    static final GetNextBlockIdsComputation instance = new GetNextBlockIdsComputation();

    private GetNextBlockIdsComputation() {}


    @Override
    JSONStreamAware processRequest(JSONObject request, Peer peer) {

        JSONObject response = new JSONObject();

        JSONArray nextBlockIds = new JSONArray();
        long blockId = Convert.parseUnsignedLong((String) request.get("blockId"));
        int limit = (int)Convert.parseLong(request.get("limit"));
        if (limit > 1440) {
            return GetNextBlocks.TOO_MANY_BLOCKS_REQUESTED;
        }
        List<Long> ids = Nxt.getTemporaryComputationBlockchain().getBlockIdsAfter(blockId, limit > 0 ? limit : 1440);
        ids.forEach(id -> nextBlockIds.add(Long.toUnsignedString(id)));
        response.put("nextBlockIds", nextBlockIds);

        return response;
    }

    @Override
    boolean rejectWhileDownloading() {
        return true;
    }

}