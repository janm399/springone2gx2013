package org.eigengo

/**
 * The package object contains type aliases for the common types that we use in the application.
 * The intention is to just give us convenient aliasing, for complete new type definitions, see the
 * ``Chunk``, ``CorrelationId`` and ``RecogSessionId`` types.
 */
package object sogx {
  // represents the incoming payload (it could be the whole image, or a bit of some movie stream)
  type ChunkData = Array[Byte]
  // represents the bytes that make up full frame in the stream
  type ImageData = Array[Byte]
  // the response is a trivial String (replace with CoinResponseModel) and remember to turn on the
  // json-to-object-transformer in the chain to make it work
  type CoinResponse = String
}
